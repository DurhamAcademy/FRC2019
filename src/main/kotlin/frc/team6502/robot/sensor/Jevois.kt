package frc.team6502.robot.sensor

import com.fazecast.jSerialComm.SerialPort
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import edu.wpi.cscore.UsbCamera
import edu.wpi.cscore.VideoMode
import edu.wpi.first.cameraserver.CameraServer
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.Notifier
import edu.wpi.first.wpilibj.Timer
import frc.team6502.robot.TIMESTEP
import java.io.Closeable
import java.nio.charset.Charset

class Jevois(private val stream: Boolean = false) : Closeable {

    private val BAUD_RATE = 115200

    // time of no data that classifies as stale
    private val STALE_THRESHOLD = 10.0

    // time to not check staleness after an initial connection, also serves as retry interval
    private val BOOT_THRESHOLD = 20.0

    // maximum reconnection attempts before giving up
    private val MAX_RETRIES = 100

    private var initializationTime = 0.0
    private var lastUpdate = 0.0

    val isDataStale
        get() = Timer.getFPGATimestamp() > lastUpdate + STALE_THRESHOLD
    val hasBootTimeExpired
        get() = Timer.getFPGATimestamp() > initializationTime + BOOT_THRESHOLD

     var jevoisPort: SerialPort? = null

    var data: LinkedTreeMap<String, Any> = LinkedTreeMap()

    init {
//        notifier.startPeriodic(TIMESTEP)

//        val cam = UsbCamera("Jevois", 0)
//        cam.setPixelFormat(VideoMode.PixelFormat.kYUYV)

//        CameraServer.getInstance().startAutomaticCapture(cam)
//        CameraServer.getInstance().addCamera(cam)
//        CameraServer.getInstance().addServer("Jevois",1181)
//        NetworkTableInstance.getDefault()
//                .getEntry("/CameraPublisher/Jevois/streams")
//                .setStringArray(arrayOf("mjpeg:http://roborio-6502-frc.local:1181/?action=stream"))





    }

    override fun close() {
        closeSerial()
    }

    protected fun finalize() {
        close()
    }

    private fun initialize() {
//        jevoisPort?.openPort()
    }

    private fun closeSerial() {
//        jevoisPort?.closePort()
    }

    fun periodic() {
/*
        if(jevoisPort == null) {
            SerialPort.getCommPorts().forEach {
                if (it.descriptivePortName.contains("ACM") && !it.isOpen) {
                    jevoisPort = it
                    jevoisPort?.openPort()
                    println("Connected to ${it.descriptivePortName}")
                }
            }
        } else {
            if(jevoisPort!!.bytesAvailable() > 0) {
                val b = ByteArray(jevoisPort!!.bytesAvailable())
                jevoisPort?.readBytes(b, b.size.toLong())
                println(b.toString(Charset.defaultCharset()))
            }
        }

*/
    }

    fun runCommand(command: String) {
//        val b = (command + "\n").toByteArray(Charset.defaultCharset())
//        jevoisPort?.writeBytes(b, b.size.toLong())
    }

}