package pj
/**
 * https://www.flowdock.com/api/team-inbox
 * 
 * The first param has to be the api key. The rest of the params are used to run a 
 * shell command that is then sent to flowdock.
 */
object SendToFlow {
  def main(args: Array[String]) {
    // Get this from flowDock:
    val apikey = args(0)
    val command = new ShellCommand()
    val result = command.run(args.tail)
    
    val content = """

        <h2>Something new and exciting is coming!</h2>
      <hr>
<pre>
        """ + 
        result.mkString("\n") +
        """
</pre>
        
        """
    
    val connection = new HttpPost("https://api.flowdock.com/v1/messages/team_inbox/" + apikey)
    connection.post(
        "source" -> "Me" ::
        "from_address" -> "pirkka.jokala@affecto.com" ::
        "subject" -> "Hello World!" ::
        "content" -> content ::
        Nil
    )
  }
}