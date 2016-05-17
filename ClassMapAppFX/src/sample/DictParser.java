package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.SynchronousQueue;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

/**
 * Created by James Davis on 12/13/2015.
 */
public class DictParser {

    private int count;
    private List list = Collections.synchronizedList(new ArrayList());
    private String searchedWord;

    private class Word {
        String head;
        String pOfSpeech;
        String definition;

        public Word(String headword, String partOfSpeech, String def)
        {
            this.head = headword;
            this.pOfSpeech = partOfSpeech;
            this.definition = def;
        }
    }

    public void searchForWord(String word)
    {
        searchedWord = word;
        String url = "http://api.pearson.com/v2/dictionaries/ldoce5/entries?headword=" + word;

        JSONObject pearsonObject = getJSONfromURL(url);
        if(pearsonObject == null)
        {
            System.err.println("Failed to load URL");
            count = 0;
            return;
        }

        long amount = (long) pearsonObject.get("count");
        count = (int) amount;


        JSONArray resultArray = (JSONArray) pearsonObject.get("results");

        for(int x = 0; x < amount; x++)
        {
            JSONObject result = (JSONObject) resultArray.get(x);

            String headword = result.get("headword").toString();

            String partOfSpeech;
            if(result.get("part_of_speech") != null)
            {
                partOfSpeech = result.get("part_of_speech").toString();
            }
            else {
                partOfSpeech = "No part of speech given";
            }

            JSONArray senseArray = (JSONArray) result.get("senses");
            JSONObject def = (JSONObject) senseArray.get(0);
            String definition;
            try{
                definition = def.get("definition").toString();
            } catch(NullPointerException e)
            {
                definition = def.toString();
            }
            definition = definition.replace("[","");
            definition = definition.replace("]","");

            list.add(new Word(headword,partOfSpeech,definition));




        }




    }

    public String getExactDefinition()
    {
        String outString = "";
        int count = 0;


        for(int x = 0; x < list.size(); x ++)
        {
            Word temp = (Word)list.get(x);
            if(temp.head.equalsIgnoreCase(searchedWord))
            {
                outString = outString +  "Headword: " + temp.head + "\nPart of Speech: " + temp.pOfSpeech + "\nDefinition: " + temp.definition + "\n\n";
            }
        }
        if(outString == null)
        {
            System.err.println("No exact definition found");
            outString = list.get(0).toString();
        }

        return outString;
    }

    public String getIndex(int index)
    {
        if(index  >= list.size())
        {
            return "Index out of range";
        }
        return (String) list.get(index);
    }


    public int getCount()
    {
        return count;
    }

    public void printResults()
    {
        for(int x = 0; x < list.size(); x++)
        {
            System.out.println(list.get(x) + "\n");
        }
    }

    private JSONObject getJSONfromURL(String url)
    {
        JSONObject pObject = null;
        try{
            String JSONString = IOUtils.toString(new URL(url));
            pObject = (JSONObject) JSONValue.parseWithException(JSONString);
        }catch(IOException | ParseException e)
        {
            return null;
        }

        return pObject;
    }
}
