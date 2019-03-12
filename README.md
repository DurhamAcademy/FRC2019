# FRC2019
Team 6502 robot code for the 2019 season.

FYI: Touching `build.gradle` or `Main.kt` will probably break things.

## Current Features
- Drivetrain
  - Drive straight assist
  - Velocity drive
  - Vision (coming soon)
  - On-RIO characterization (kV and kS)
  - Ramsete spline following
- Elevator
  - Trapezoidal height control for maximum smoothness
  - Minimal driver control (elevator knows what game piece it has and moves accordingly)
- Cargo intake
  - Automatic shutoff when cargo has been acquired
- Hatch intake
  - Moves hatch panels around.
  - Coordinated autonomous elevator movement to pick up and place panels with one button
