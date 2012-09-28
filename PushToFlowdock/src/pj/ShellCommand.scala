package pj

import java.lang.Process
import java.lang.ProcessBuilder
import java.util.ArrayList
import java.io.InputStream
import scala.io.Source
import java.lang.Thread

object ShellCommand {
  def main(args: Array[String]) {
    new ShellCommand().run("free" :: Nil).foreach(l=>println(l))
  }
}

class ShellCommand {
  def run(command : Seq[String]) : Seq[String] = {
    val arrayList = new ArrayList[String]()
    command.foreach(arrayList.add(_))
    val processBuilder = new ProcessBuilder(arrayList)
    
    val process = processBuilder.start()
    val stdout = new StreamReaderThread(process.getInputStream())
    val stderr = new StreamReaderThread(process.getErrorStream())
    if (process.waitFor == 0) {
      Thread.sleep(1000)
//      println("Success")
      stdout.contents
    } else {
//      println("Fail")
      throw new RuntimeException("Process failed: \n" + stderr.contents.mkString("\n"))
    }
  }
}

class StreamReaderThread(in : InputStream) extends Thread {
  var contents = Seq[String]()
  start()
  
  override def run() : Unit = {
    val s = Source.fromInputStream(in)
    try {
      contents = s.getLines.toList
    } finally {
      s.close
    }
//    println("Thread done, result: " + contents)
  }
}