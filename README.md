How to build:<br />
Open a command prompt/shell in the launcher directory<br />
Run the command 'gradlew eclipse' or (mac/linux) './gradlew eclipse'<br />
Open eclipse and import existing eclipse project from the launcher directory<br />

How to run:<br />
Open a command prompt/shell in the launcher directory<br />
Run the command 'gradlew clean build publish run' or (mac/linux) './gradlew clean build publish run'<br />

<br />
How it works:<br />
Step 1: bootstrap - com.liquidforte.launcher.bootstrap.Bootstrap class downloads required libraries and relauches the JVM, with the downloaded files now on the classpath and using com.liquidforte.launcher.Launcher as the main class<br />
Step 2: Launch - the com.liquidforte.launcher.Launcher class reads the configuration(run/conf/launch.conf), determining the repositories, artifacts, main class and arguments<br />
Step 3: Invoke Ivy - Launcher class invokes the Apache Ivy library to manage configuration dependencies<br />
Step 4: Finalize - the Launcher class gets the classpath for the final launch step and invokes a new JVM<br />