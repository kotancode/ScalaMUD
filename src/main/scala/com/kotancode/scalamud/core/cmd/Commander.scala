package com.kotancode.scalamud.core.cmd

import akka.actor._
import akka.routing._

import com.kotancode.scalamud.core.lang.EnrichedWord
import java.util.ArrayList
import edu.stanford.nlp.ling.Sentence
import edu.stanford.nlp.ling.TaggedWord
import edu.stanford.nlp.ling.HasWord
import edu.stanford.nlp.tagger.maxent.MaxentTagger
import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer
import com.kotancode.scalamud.core.cmd._

class Commander extends Actor {
	def receive = {
		case s:String => {
			val words = s.split(" ")
			val wordList = new java.util.ArrayList[String]()
			for (elem <- words) wordList.add(elem)
		    val sentence = Sentence.toWordList(wordList)
		    val taggedSentence = Commander.tagger.tagSentence(sentence).asScala.toList
		
			var enrichedWords = new ListBuffer[EnrichedWord]
		    for (tw : TaggedWord <- taggedSentence) {
				val ew = EnrichedWord(tw)
				println(ew)
				enrichedWords += ew
			}
			
			sender ! HandleCommand(EnrichedCommand(enrichedWords, sender))
	}
  }
}

object Commander {
	val tagger = new MaxentTagger("models/english-bidirectional-distsim.tagger")
}