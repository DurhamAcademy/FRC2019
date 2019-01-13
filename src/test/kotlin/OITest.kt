package frc.team6502.robot
import org.junit.Assert.*

class OITest {

    @org.junit.Before
    fun initialize() {
        OI
    }

    /**
     * Sicko mode or mo bamba?
     */
    @org.junit.Test
    fun testControllerZeroes() {
        assertEquals(0.0, OI.controller.x, 0.0)
        assertEquals(0.0, OI.controller.y, 0.0)
    }

    @org.junit.Test
    fun testDeadband() {
        assertEquals("Not exceeding deadband, zeroing", 0.0, OI.deadband(0.1, 0.2), 0.0)
        assertEquals("Exceeding deadband, retaining original input",0.3, OI.deadband(0.3, 0.2), 0.0)

        assertNotEquals("Ensuring good value is not zeroed out", 0.0, OI.deadband(0.5, 0.25), 0.0)
        assertNotEquals("Makes sure bad values are not left", 0.7, OI.deadband(0.7, 0.8), 0.0)
    }
}