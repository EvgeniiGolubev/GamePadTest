# GamePadTest
The project is built to show how to get Java and a game controller to work together. It is based on the jinput library. The GUI is built using javafx.
In this project, I expanded the functionality of the library by adding the ability to connect and disconnect the controller at any time the program is running.
To check the performance, the controller from the xbox 360 was used.
<p>Link to the repository jinput: https://github.com/jinput/jinput 
<p>
<h2> How to run the project </h2>
<ul>
  <li>Run in command line:
    <ul><li><code>mvn compile exec:exec</code></li></ul>
  </li>
  <p>
  <li>Or create a jar and run it on the command line by adding the path to the native jinput library:
    <ul>
      <li><code>mvn package</code></li>
      <li><code>java "-Djava.library.path=natives" -jar GamePadTest-1.0-shaded.jar</code></li>
    </ul>
  </li>
</ul>
