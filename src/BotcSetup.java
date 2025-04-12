
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Random;
import java.lang.Math;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;


class BotcSetup {

    private static Random rand = new Random();
    private static int iRand;
    private static final String[] teams = new String[]{"townsfolk", "outsider", "minion", "demon"};
    private static int t;

    private String[][] characters;

    private boolean hunts0 = false;
    private boolean athDupe = false;
    private boolean kazAll = false;
    private int[] kazRange = new int[] {1, 2};
    private boolean lotGarden = true;
    private boolean lotAll = false;
    private int[] lotRange = new int[] {0, 1};
    private boolean legAll = false;
    private boolean legOut = false;
    private int athPercent = 25;

    public BotcSetup(boolean all, String[][] charactersS) {
        if (all) {
            hunts0 = true;
            athDupe = true;
            kazAll = true;
            lotGarden = false;
            lotAll = true;
            boolean legAll = true;
            boolean legOut = true;
            athPercent = 50;
        }
        characters = charactersS;
    }

    public BotcSetup(String[][] charactersS, boolean hunts0S, boolean athDupeS, boolean kazAllS, int[] kazRangeS, boolean lotGardenS, boolean lotAllS, int[] lotRangeS, boolean legAllS, boolean legOutS, int athPercentS) {
        hunts0 = hunts0S;
        athDupe = athDupeS;
        kazAll = kazAllS;
        kazRange = kazRangeS;
        lotGarden = lotGardenS;
        lotAll = lotAllS;
        lotRange = lotRangeS;
        legAll = legAllS;
        legOut = legOutS;
        athPercent = athPercentS;

        characters = charactersS;
    }

    public static BotcSetup makeSetupGenerator() throws FileNotFoundException, IOException, ParseException {

        String scriptName = Storyteller.readAsk("What is the name of your script json? " +
                "Program will break if you put it in wrong.");
        if (!(scriptName.substring(scriptName.length() - 5, scriptName.length()).equals(".json"))) {
            scriptName += ".json";
        }

        Object oScript = new JSONParser().parse(new FileReader(scriptName));
        JSONArray aScript = (JSONArray) oScript;




        String[][] charactersC = new String[4][1];
        int lastCut = 1;
        String[] charactArr = new String[aScript.size()];
        for (int i = 1; i < aScript.size(); i++) {
            String charname = (String) (((JSONObject) aScript.get(i)).get("name"));
            charactArr[i] = charname;
            if ( !(((String) (((JSONObject) aScript.get(i)).get("team"))).equals(teams[t]))) {
                charactersC[t] = Arrays.copyOfRange(charactArr, lastCut, i);
                t++;
                lastCut = i;
            }
        }
        charactersC[t] = Arrays.copyOfRange(charactArr, lastCut, charactArr.length);

        String[] options = new String[] {"All", "Recommended", "Custom", "Details"};
        String type = Storyteller.ask("Choose your mode of randomization. Select Details for details.", options);

        boolean hunts0C = false;
        boolean athDupeC = false;
        boolean kazAllC = false;
        int[] kazRangeC = new int[] {1, 2};
        boolean lotGardenC = true;
        boolean lotAllC = false;
        int[] lotRangeC = new int[] {0, 1};
        boolean legAllC = false;
        boolean legOutC = false;
        int athPercentC = 25;

        String answer = new String("");
        if (type.equals("all")) {
            return new BotcSetup(true, charactersC);
        } else if (type.equals("recommended")) {
            return new BotcSetup(false, charactersC);
        } else if (type.equals("custom")) {

            if (Storyteller.search(charactersC[3], "Kazali")) {
                answer = Storyteller.readAsk("You have Kazali in your script. " +
                        "Allow all possible Kazali setups?");
                if (answer.equals("y") || answer.equals("yes")) {
                    kazAllC = true;
                } else {
                    kazRangeC[0] = Storyteller.ask("What is the lowest amount of Outsiders Kazali should add? " +
                            "(Type a negative number for removal)", -2, charactersC[1].length);
                    kazRangeC[1] = Storyteller.ask("What is the highest amount of Outsiders Kazali should add? " +
                            "(Type a negative number for removal)", kazRangeC[0], charactersC[1].length);
                }
            }

            if (Storyteller.search(charactersC[3], "Lord of Typhon")) {
                answer = Storyteller.readAsk("You have Lord of Typhon in your script. " +
                        "Would you like to generate Lord of Typon setups assuming Gardener is in play?");
                if (!(answer.equals("y") || answer.equals("yes"))) {
                    lotGardenC = false;
                    answer = Storyteller.readAsk("Allow all possible Lord of Typhon setups?");
                    if (answer.equals("y") || answer.equals("yes")) {
                        lotAllC = true;
                    } else {

                        lotRangeC[0] = Storyteller.ask("What is the lowest amount of Outsiders Lord of Typhon should add? " +
                                "(Type a negative number for removal)", -2, charactersC[1].length);
                        lotRangeC[1] = Storyteller.ask("What is the highest amount of Outsiders Lord of Typhon should add? " +
                                "(Type a negative number for removal)", lotRangeC[0], charactersC[1].length);
                    }
                }
            }

            if (Storyteller.search(charactersC[3], "Legion")) {
                answer = Storyteller.readAsk("You have Legion in your script. " +
                        "Allow all possible Legion counts?");
                if (answer.equals("y") || answer.equals("yes")) {
                    legAllC = true;
                }
                answer = Storyteller.readAsk("Allow Outsiders in Legion setups?");
                if (answer.equals("y") || answer.equals("yes")) {
                    legOutC = true;
                }
            }

            if (Storyteller.search(charactersC[0], "Huntsman")) {
                answer = Storyteller.readAsk("You have Huntsman in your script. " +
                        "Would you like Huntsman to be included in 0 outsider setups?");
                if (answer.equals("y") || answer.equals("yes")) {
                    hunts0C = true;
                }
            }

            if (Storyteller.search(charactersC[0], "Atheist")) {
                athPercentC = Storyteller.ask("You have Atheist in your script. " +
                        "What percentage of games would you like to be Atheist games?", 0, 100);
                answer = Storyteller.readAsk("Allow Atheist setups to have duplicate characters?");
                if (answer.equals("y") || answer.equals("yes")) {
                    athDupeC = true;
                }
            }
        }

        return new BotcSetup(charactersC, hunts0C, athDupeC, kazAllC, kazRangeC, lotGardenC, lotAllC, lotRangeC, legAllC, legOutC, athPercentC);


    }

    public void manySetups() {
        String stop = Storyteller.readAsk("Type stop at the end of a setup to stop generation.");
        while (!stop.equals("stop")) {
            if (rand.nextInt(100) < athPercent && Storyteller.search(characters[0], "Atheist")) {
                String[][] setup = makeAthSetup(characters, athDupe);
                printSetup(setup, characters);
            } else {
                String[][] setup = makeSetup();
                printSetup(setup, characters);
            }
            stop = Storyteller.readAsk("");
        }
    }

    public static String[][] makeAthSetup(String[][] characters, boolean athDupe) {

        String[][] setup = new String[4][0];

        int playCount = Storyteller.ask("How many players?", 5, 15);

        String[] goodChars = new String[characters[0].length + characters[1].length];

        for (int i = 0; i < goodChars.length; i++) {
            if (i < characters[0].length) {
                goodChars[i] = characters[0][i];
            } else {
                goodChars[i] = characters[1][i - characters[0].length];
            }
        }

        int tCount = 1; //The Atheist added in at the start
        int oCount = 0;

        String[] gArr = new String[playCount];
        Arrays.fill(gArr, "");
        gArr[0] = "Atheist";
        for (int i = 1; i < playCount; i++) {
            iRand = rand.nextInt(goodChars.length);
            while ((Storyteller.search(gArr, goodChars[iRand])) && !athDupe) {
                iRand = rand.nextInt(goodChars.length);
            }
            gArr[i] = goodChars[iRand];
            if (Storyteller.search(characters[0], gArr[i])) {
                tCount ++;
            } else {
                oCount ++;
            }
        }

        int to = 0;
        int ou = 0;
        String[] tArr = new String[tCount];
        String[] oArr = new String[oCount];
        for (int i = 0; i < gArr.length; i++) {
            if (Storyteller.search(characters[0], gArr[i])) {
                tArr[to] = gArr[i];
                to++;
            } else {
                oArr[ou] = gArr[i];
                ou++;
            }
        }

        setup[0] = tArr;
        setup[1] = oArr;

        return setup;


    }

    public String[][] makeSetup() {

        String[][] setup = new String[4][0];

        int playCount = Storyteller.ask("How many players?", 5, 15);
        Storyteller.read(Integer.toString(playCount));
        int tCount = 3 + (playCount - 4) / 3 * 2;
        int oCount = (playCount - 7) % 3;
        int mCount = (playCount - 4) / 3;
        if (playCount < 7) {
            tCount = 3;
            oCount = playCount - 5;
            mCount = 1;
        }
        int dCount = 1;

        int[] baseCount = new int[]{tCount, oCount, mCount, dCount}; //for reference later

        Storyteller.read("T: " + Integer.toString(tCount) + "\nO: "
                + Integer.toString(oCount) + "\nM: " + Integer.toString(mCount)
                + "\nD: " + Integer.toString(dCount));

//        String[] goodChars = new String[characters[0].length + characters[1].length];
//        for (int i = 0; i < goodChars.length; i++) {
//            if (i < characters[0].length) {
//                goodChars[i] = characters[0][i];
//            } else {
//                goodChars[i] = characters[1][i - characters[0].length];
//            }
//        }


        String[] dArr = new String[dCount];
        Arrays.fill(dArr, "");
        for (int i = 0; i < dCount; i++) {
            iRand = rand.nextInt(characters[3].length);
            while ((Storyteller.search(dArr, characters[3][iRand]))) {
                iRand = rand.nextInt(characters[3].length);
            }



            dArr[i] = characters[3][iRand];
        }
        //Storyteller.read(dArr[0]);
        if (Storyteller.search(dArr, "Vigormortis")) {
            if (oCount > 0) {
                oCount -= 1;
                tCount += 1;
            }
        }
        if (Storyteller.search(dArr, "Fang Gu")) {
            if (tCount > 0) {
                oCount += 1;
                tCount -= 1;
            }
        }
        if (Storyteller.search(dArr, "Lil' Monsta")) {
            mCount += 1;
        }

        int mod = 0;
//        if (Storyteller.search(dArr, "Kazali")) {
//
//            tCount += mCount;
//            mCount = 0;
//
//            if (mod < -oCount) {
//                mod = -oCount;
//            }
//            if (mod > tCount) {
//                mod = Math.min(tCount, characters[1].length - oCount);
//            }
//            tCount -= mod;
//            oCount += mod;
//        }

        //LOT AND KAZALI HAVE A BUG! When tCount is higher than number of townsfolk on script
        if (Storyteller.search(dArr, "Lord of Typhon") || Storyteller.search(dArr, "Kazali")) {
            //replace all minions with townsfolk, get a random modifier
            //make sure it doesn't go under current amount of outsiders
            //make sure it doesn't go over current amount of townsfolk
            //subtract that much from townsfolk, add that much to outsiders
            tCount += mCount;
            mCount = 0;
            int tAdj = 0; //ugggggggh
            if (Storyteller.search(characters[0], "Atheist")) {
                tAdj = -1;
                //basically, I need to use characters[0].length to refer to the count of all townsfolk on script
                //but if atheist is on script, they shouldn't be included in that count because they can't be in the setup
                //but I can't just remake characters[0] without atheist cause I need them in it for Drunk
                //so this is what I got for now
            }
            if (Storyteller.search(characters[0], "Village Idiot")) {
                tAdj += 2;
            }
            if (Storyteller.search(dArr, "Lord of Typhon") && lotGarden) {
                mCount += 1;
                if (rand.nextInt(tCount + oCount) < oCount) {
                    oCount -= 1;
                } else {
                    tCount -= 1;
                }
            } else if (Storyteller.search(dArr, "Lord of Typhon")) {
                if (lotAll) {
                    mod = rand.nextInt(-oCount, characters[1].length + 1 - oCount);
                } else {
                    mod = rand.nextInt(lotRange[0], lotRange[1] + 1);
                }
            } else if (Storyteller.search(dArr, "Kazali")) {
                if (kazAll) {
                    mod = rand.nextInt(-oCount, characters[1].length + 1);
                } else {
                    mod = rand.nextInt(kazRange[0], kazRange[1] + 1);
                }
            }
            if (mod < -oCount) {
                mod = -oCount;
            }
            if (mod + oCount > characters[1].length) {
                mod = characters[1].length - oCount;
            }
            if (mod > tCount) {
                mod = Math.min(tCount, characters[1].length - oCount);
            }
            //Storyteller.read("Outsiders " + oCount + " Townsfolk " + tCount);
            if (tCount - mod > characters[0].length + tAdj) {
                mod = tCount - (characters[0].length + tAdj); //the problem is ATHEIST!!!
            }
            tCount -= mod;
            oCount += mod;
        }
        if (Storyteller.search(dArr, "Riot")) {
            mCount = 0;
        }
        if (Storyteller.search(dArr, "Legion")) {
            if (legAll) {
                mCount = 0;
                tCount = rand.nextInt(2, (playCount + 1) / 2); //pCount + 1 to handle odd numbers
                if (legOut) {
                    oCount = rand.nextInt(0, (playCount + 1) / 2 - tCount);
                    if (oCount > baseCount[1]) {
                        oCount = baseCount[1];
                    }
                } else {
                    oCount = 0;
                }
            } else {
                tCount = mCount + dCount;
                mCount = 0;
                if (legOut && baseCount[1] > 0) {
                    oCount = rand.nextInt(0, 2);
                } else {
                    oCount = 0;
                }
            }
        }

        //Storyteller.read("Outsiders " + oCount + " Townsfolk " + tCount);

        String[] mArr = new String[mCount];
        Arrays.fill(mArr, "");
        for (int i = 0; i < mCount; i++) {
            iRand = rand.nextInt(characters[2].length);
            if (i == 3 && characters[2].length == 4 && Storyteller.search(characters[2], "Summoner")) {
                Storyteller.read("Put more minions on your script, idiot!");
            }
            while ((Storyteller.search(mArr, characters[2][iRand]))
                    || (characters[2][iRand].equals("Summoner") && mCount > baseCount[2])) {
                iRand = rand.nextInt(characters[2].length);
            }
            mArr[i] = characters[2][iRand];
        }

        if (Storyteller.search(mArr, "Summoner")) {
            mCount = baseCount[2];
            if (mArr.length > mCount) {
                mArr = Arrays.copyOfRange(mArr, 0, mArr.length - 1);
            }

            //reset demon modifications, add extra townsfolk
            tCount = baseCount[0] + 1;
            oCount = baseCount[1];
            dCount = 0;
            dArr = new String[0];

            //Same applies to Summoner as to Marionette
        }
        if (Storyteller.search(mArr, "Marionette")) {
            tCount ++;

            //Marionette wiki says to only add a townsfolk token, but if it could add outsiders I'd do this:
//                if (rand.nextInt(characters[0].length + characters[1].length) < characters[0].length) {
//                    tCount += 1;
//                } else {
//                    oCount += 1;
//                }
        }
        if (Storyteller.search(mArr, "Baron")) {
            oCount += 2;
            tCount -= 2;
        }
        if (Storyteller.search(mArr, "Godfather")) {
            if (rand.nextInt(2) == 1 || oCount == 0) {
                oCount += 1;
                tCount -= 1;
            } else if (oCount > 0) {
                oCount -= 1;
                tCount += 1;
            }
        }

        if (tCount < 0) {
            tCount = 0;
        }

        int vi = 0; //Village Idiot counter
        String[] tArr = new String[tCount];
        Arrays.fill(tArr, "");
        for (int i = 0; i < tCount; i++) {
            iRand = rand.nextInt(characters[0].length);
            while ((Storyteller.search(tArr, characters[0][iRand]) && (!characters[0][iRand].equals("Village Idiot") || vi == 3))
                    || (characters[0][iRand].equals("Huntsman") && !hunts0 && oCount == 0)
                    || characters[0][iRand].equals("Atheist")) {
                iRand = rand.nextInt(characters[0].length);
            }
            tArr[i] = characters[0][iRand];
            if (characters[0][iRand].equals("Village Idiot")) {
                vi++;
            }

            //Not sure a better way to repeat a for loop early, so I'm just replacing it at end
            if (Storyteller.search(tArr, "Choirboy") && !Storyteller.search(tArr, "King")) {
                tArr[i] = "King";
            }
        }
        if (Storyteller.search(tArr, "Balloonist")) {
            if (rand.nextInt(2) == 1 && oCount < characters[1].length) {
                oCount += 1;
                tCount -= 1;
                if (!tArr[tArr.length - 1].equals("Balloonist")) {
                    tArr = Arrays.copyOfRange(tArr, 0, tArr.length - 1);
                } else {
                    tArr = Arrays.copyOfRange(tArr, 1, tArr.length);
                }
            }
        }
        if (Storyteller.search(tArr, "Huntsman") && hunts0 && oCount == 0) {
            oCount += 1;
            tCount -= 1;
            if (!tArr[tArr.length - 1].equals("Huntsman")) {
                tArr = Arrays.copyOfRange(tArr, 0, tArr.length - 1);
            } else {
                tArr = Arrays.copyOfRange(tArr, 1, tArr.length);
            }


        }

        String[] oArr = new String[oCount];
        Arrays.fill(oArr, "");
        for (int i = 0; i < oCount; i++) {
            iRand = rand.nextInt(characters[1].length);
            while ((Storyteller.search(oArr, characters[1][iRand]))) {
                iRand = rand.nextInt(characters[1].length);
            }
            oArr[i] = characters[1][iRand];
            if (Storyteller.search(tArr, "Huntsman") && !Storyteller.search(oArr, "Damsel")) {
                oArr[i] = "Damsel";
            }
        }

        setup[0] = tArr;
        setup[1] = oArr;
        setup[2] = mArr;
        setup[3] = dArr;


        return setup;
    }

    public static void printSetup(String[][] setup, String[][] characters) {
        int t = 0;

        for (String[] team : setup) {
            String teamChars = new String(teams[t]);
            if (t == 1 || t == 2) {
                teamChars += "s";
            }
            teamChars += ": ";
            for (String charName : team) {
                teamChars += charName + ", ";
            }
            Storyteller.read(teamChars);
            t++;
        }

        if (Storyteller.search(setup[1], "Drunk")) {
            iRand = rand.nextInt(characters[0].length);
            while ((Storyteller.search(setup[0], characters[0][iRand]))) {
                iRand = rand.nextInt(characters[0].length);
            }
            String drunkSees = characters[0][iRand];
            Storyteller.read("The Drunk sees themself as the " + drunkSees);
        }

        if (Storyteller.search(setup[1], "Lunatic")) {
            iRand = rand.nextInt(characters[3].length);
            while (characters[3][iRand].equals("Lil' Monsta")) {
                iRand = rand.nextInt(characters[3].length);
            }
            String lunaticSees = characters[3][iRand];
            Storyteller.read("The Lunatic sees themself as the " + lunaticSees);
        }

        if (Storyteller.search(setup[2], "Evil Twin")) {
            //String[] goodChars = Array.
            iRand = rand.nextInt(setup[0].length);
            String goodTwin = setup[0][iRand];
            Storyteller.read("The Good Twin is the " + goodTwin);
        }
    }
}

