package com.nlp.example;

import java.io.BufferedWriter;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import antlr.StringUtils;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/*
 * Developer - Pininti Sai Pravalik Reddy
*/
public class TokenizeExample {

	public static void main(String[] args) throws IOException {
		String propertiesName = "tokenize, ssplit";
		Properties props = new Properties();
		props.setProperty("annotators", propertiesName);
		System.out.println(
				"Enter the name of the file that needs to be processed. Note: Here all files are present in root folder. Directly give the name of the file with extension");
		System.out.println("eg.-> data/Abdominal-Bump ");
		Scanner scan = new Scanner(System.in);
		String filename = scan.nextLine();
		String text = TokenizeExample.getFileContent(filename);
		BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"));
		BufferedWriter bwbw = new BufferedWriter(new FileWriter("actualoutput.txt"));
		/*
		 * STEP2 Creating a StanfordCorNLP Instance and tokenizing using Natuaral
		 * Language Processing.
		 */
		try {
			List<String> keylist = new ArrayList();
			keylist = TokenizeExample.getKeywords("keywordsduplicate.txt");
			Map<String, Integer> keywordInSentences = new HashMap();
			Map<String, Float> tfmap = new HashMap();
			Map<String, Integer> mapmap = new HashMap();
			for (String ll : keylist) {
				keywordInSentences.put(ll, 0);
			}
			StanfordCoreNLP stanfordCoreNLP = new StanfordCoreNLP(props);
			CoreDocument coreDocument = new CoreDocument(text);
			stanfordCoreNLP.annotate(coreDocument);
			List<CoreSentence> sentences = coreDocument.sentences();

			/* STEP3 Iterating over the sentences in the file given as input. */
			int sentenceCount =0;
			//This represents a keyword appeared in how many no of sentences.
			
			for (CoreSentence sentence : sentences) {
				sentenceCount++;
				/* Printing the Sentence. */
				System.out.println(sentence.toString());
				List<String> listlist = new ArrayList();
				listlist = TokenizeExample.getKeywords("keywordsduplicate.txt");
				for (String ll : listlist) {
					int cnt = (sentence.toString().split(ll, -1).length) - 1;
					mapmap.put(ll, cnt);
					// total number of words in the above sentence.
					int totalwords = convertSentenceToStrings(sentence).size();
					float tf = (float)cnt/(totalwords-1);
					tfmap.put(ll, tf);
					if (cnt!=0) {
							keywordInSentences.put(ll, keywordInSentences.get(ll)+1);
					}
				}
				try {
					bwbw.write(sentence.toString());
					bwbw.write("\n");
					bwbw.write(mapmap.toString());
					bwbw.write("\n");
					bwbw.write(tfmap.toString());
					bwbw.write("\n");
					bwbw.write(keywordInSentences.toString());
					bwbw.write("\n");
					System.out.println("Successfully written to a file");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				/*
				 * Finding the occurence of each keyword and storing them in a MAP with
				 * Key=keyword, value=occurence
				 */
				Map<String, Integer> map = new HashMap();
				List<String> alist = convertSentenceToStrings(sentence);
				for (String keyword : alist) {
					int count = 0;
					for (String match : alist) {
						if (match.equals(keyword)) {
							count++;
						}
					}
					map.put(keyword, count);
				}
				System.out.println("map is: " + map);
				System.out.println();
				try {
					bw.write(sentence.toString());
					bw.write("\n");
					bw.write(map.toString());
					bw.write("\n");
					;
					System.out.println("Successfully written to a file");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			System.out.println("sentenceCount "+sentenceCount);
			bw.close();
			bwbw.close();
			System.out.println("keyword in how many sentences: ");
			System.out.println(keywordInSentences);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public double tf(List<CoreSentence> doc, String term) {
		double result = 0;
		for (CoreSentence word : doc) {
			if (term.equalsIgnoreCase(word.toString()))
				result++;
		}
		return result / doc.size();
	}

	public static String getFileContent(String str) {
		// String str = "data/Abdominal-Bump.txt";
		String text = new String();
		File file = new File(str);
		/* STEP1 Loading file in to the string called text: */
		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNext()) {
				text = text.concat(sc.nextLine());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return text;
	}

	public static ArrayList getKeywords(String str) {

		String keywords = new String();
		List<String> list = new ArrayList<String>();
		File keyfile = new File(str);
		int i = 0;
		try {
			Scanner sc = new Scanner(keyfile);
			while (sc.hasNext()) {
				list.add(sc.nextLine());
				// System.out.println(list.get(i++));
				// keywords = keywords.concat(sc.nextLine());
			}
			// System.out.println(keywords);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return (ArrayList) list;

	}
	
	public static ArrayList<String> convertSentenceToStrings(CoreSentence sentence) {
		CoreDocument cd = new CoreDocument(sentence.toString());
		String propertiesName = "tokenize, ssplit";
		Properties props = new Properties();
		props.setProperty("annotators", propertiesName);
		StanfordCoreNLP stanfordCord = new StanfordCoreNLP(props);
		stanfordCord.annotate(cd);

		/* STEP4 Extracting tokens (words) from the sentence. */
		List<CoreLabel> coreLabelList = cd.tokens();
		List<String> alist = new ArrayList<String>();
		for (CoreLabel coreLabel : coreLabelList) {
			alist.add(coreLabel.originalText());
		}
		return (ArrayList)alist;
	}

}
