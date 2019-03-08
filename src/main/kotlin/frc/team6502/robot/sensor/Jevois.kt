package frc.team6502.robot.sensor

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import edu.wpi.cscore.UsbCamera
import edu.wpi.cscore.VideoMode
import edu.wpi.first.cameraserver.CameraServer
import edu.wpi.first.wpilibj.*
import frc.team6502.robot.TIMESTEP
import java.io.Closeable

class Jevois(private val stream: Boolean = false) : Closeable {

    private val BAUD_RATE = 115200

    // time of no data that classifies as stale
    private val STALE_THRESHOLD = 0.1

    // time to not check staleness after an initial connection, also serves as retry interval
    private val BOOT_THRESHOLD = 2.0

    // maximum reconnection attempts before giving up
    private val MAX_RETRIES = 100

    private var initializationTime = 0.0
    private var lastUpdate = 0.0

    val isDataStale
        get() = Timer.getFPGATimestamp() > lastUpdate + STALE_THRESHOLD
    val hasBootTimeExpired
        get() = Timer.getFPGATimestamp() > initializationTime + BOOT_THRESHOLD

    private var jevoisPort: SerialPort? = null
    private var retries = 0
    private val notifier = Notifier { periodic() }

    var data: LinkedTreeMap<String, Any> = LinkedTreeMap()

    init {
        notifier.startPeriodic(TIMESTEP)

        val cam = UsbCamera("Jevois", 0)
        cam.setPixelFormat(VideoMode.PixelFormat.kYUYV)
        if (stream) CameraServer.getInstance().startAutomaticCapture(cam)
    }

    override fun close() {
        closeSerial()
    }

    protected fun finalize() {
        close()
    }

    private fun initialize() {
        jevoisPort?.enableTermination()
        retries = 0
        initializationTime = Timer.getFPGATimestamp()
    }

    private fun closeSerial() {
        data.clear()
        jevoisPort?.close()
        jevoisPort = null
    }

    private fun periodic() {


        // if there is no data after a while then the cam is probably ded


        if (jevoisPort == null) {
            // camera is not connected, attempt connection
            if (hasBootTimeExpired) {
                try {
                    jevoisPort = SerialPort(BAUD_RATE, SerialPort.Port.kUSB)
                    initialize()
                } catch (e: Exception) {

                    DriverStation.reportWarning("Jevois refused serial connection, retry ${++retries}/$MAX_RETRIES", false)
                    initializationTime = Timer.getFPGATimestamp()

                }
            }
        } else {
            // camera is connected, read data
            if (jevoisPort!!.bytesReceived > 0) {
                lastUpdate = Timer.getFPGATimestamp()
                val s = jevoisPort!!.readString().trim()
                if (s.startsWith("{") && s.endsWith("}")) {
                    // that boi is a json
                    data = Gson().fromJson(s, data.javaClass)
                } else {
                    // either malformed or a log message
                    println("JEVOIS: $s")
                }
            }

        }

        if (isDataStale) closeSerial()
        if (retries >= MAX_RETRIES) {
            notifier.stop()
            closeSerial()
            DriverStation.reportError("Jevois exceeded maximum retry count, disabling vision system!!!", false)
            return
        }
    }

    fun runCommand(command: String) {
        if (jevoisPort != null) {
            jevoisPort?.writeString("$command\n")
        } else {
            DriverStation.reportWarning("Tried executing command on disconnected serial port", false)
        }
    }

}