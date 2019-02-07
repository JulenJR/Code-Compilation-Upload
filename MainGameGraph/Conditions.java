package com.mygdx.safe.MainGameGraph;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.safe.Pair;


import java.util.HashMap;

import static com.badlogic.gdx.Gdx.app;
import static java.lang.System.exit;


/**
 * Created by Boris.InspiratGames on 30/10/17.
 */

public class Conditions {

    private static final String TAG = Conditions.class.getSimpleName();
    private boolean checkConditions = true;
    private String idInstructionsNames = "OR%AND%ID_";
    private String firsinstructionNames = "CHOICER%SHOOTER%DIRECT%EVALUATOR%COUNTER%TIMER%SEQUENCE%RANDOM%MOVEPROG%VAR%none";
    private String thirdComprobationNames = "CHOICER%SHOOTER%DIRECT%EVALUATOR%COUNTER%TIMER%SEQUENCE%RANDOM";


    String[] conditions;
    HashMap<String, SpecialSuperTree> shooterTrees;
    // conds: AND$ID_1@ID_2$TIMER_1@30@ENTITY#GUARD_00#changeToCircunscription#GUARD_00#1#2@ENTITY#GUARD_00#changeToCircunscription#GUARD_00#1#1$none$none
    // conds: AND_OR
    //            $ID_1@ID_2@ID_3@actionConsequence               // cada ID es una condicion (ActionChoice) necesaria para activar el ToActionNode;
    //
    //        $TIMER_1@init@limit@step@preAction@PostAction@active
    // TIMER_X es el nombre del timer.
    // init: inicio (en segundos) del contador de tiempo (timer)
    // limit: final (en segundos) del contador de tiempo (timer)
    // step:  especifica si la cuenta es creciente (timer creciente) o decreciente (crono)
    // preAction@PostAction : especifica la accion previa y la posterior a la ejecucion del timer
    // active: determina si el timer inicia activado o no (si no está activado, otro evento deberá activarlo)

    //        $TIMER_1@time@preAction@PostAction // TIMER_X es el nombre del timer.
    //                                           // time es en  segundos (simplificado: empieza en time, acaba en 0, es decreciente (crono)
    //                                           //                       su nombre es un ID automático que no permite identificarlo con facilidad,
    //                                           //                       y está activo por defecto)
    //                                           // preaction : lo que ocurre cuando empieza el evento: ej: GUARD_00#changeToCircunscription#GUARD_00#1#2
    //                                           // postaction: lo que ocurre cuando termina el evento: ej: GUARD_00#changeToCircunscription#GUARD_00#1#1
    //
    //        $RANDOM_1@randomNumber@RandomElection@ActionConsequence   // Cada RANDOM_X contiene un numero de posibilidades, y la elegida para hacerlo true;
    //
    //        COUNTER_CONDITIONS
    //        $COUNTER==3@ActionConsequence
    //
    //
    // # <- Instruction Commands separator
    // $ <- Diferent type Instruction Separator
    // @ <- Same type instruction Separator

    boolean setConditions = false;
    boolean or_compare = false;
    boolean and_compare = false;
    public boolean with_idsCompare;
    public boolean with_timers;
    public boolean with_randoms;
    public boolean with_counters;
    public boolean with_vars;
    public boolean with_evaluators;
    public boolean with_shooters;
    public boolean with_directInstructions;
    public boolean with_moveprogs;
    public boolean with_sequences;
    public boolean with_choicers;
    public ToActionNode n;
    public HashMap<String, Boolean> _id_activation;
    public HashMap<String, String[]> _evaluators;
    public HashMap<String, Pair<Integer, String[]>> _shooters;
    public HashMap<String, String[]> _directInstructions;


    public HashMap<String, HashMap<String, Array<String[]>>> counterInstructions = new HashMap<String, HashMap<String, Array<String[]>>>();
    public HashMap<String, Pair<String, String>> _varMap = new HashMap<String, Pair<String, String>>();
    public HashMap<String, String> counterShooterRelation = new HashMap<String, String>();
    public HashMap<String, ProgrammedMovement> _moveprogs = new HashMap<String, ProgrammedMovement>();
    public HashMap<String, Choicer> _choicers = new HashMap<String, Choicer>();


    public HashMap<String, String[]> randoms;
    public Array<String> idInstructions = new Array<String>();
    String conds;
    com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;
    GameGraph gameGraphCondition;


    public Conditions(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g) {
        this.g = g;
    }

    public void setConditions(ToActionNode n, String conds, GameGraph gg, HashMap<String, com.mygdx.safe.MainGameGraph.Timer> timers, HashMap<String, SequenceInstructions> sequences, GameGraph ggraph) {
        gameGraphCondition=ggraph;
        if (!setConditions) {
            this.n = n;
            if (checkConditions) {
                String newconds = sintaxBasicChecker(conds, n.getID(), g.m.ggMgr.isNecesaryConditionsBasicCheck);
                if (newconds == null) {
                    //System.out.println(TAG +" CONDITIONS DATA:["+conds+"]");
                    System.out.println("\n"+TAG + " SINTAX ERROR AT: node:" + n.getID() + " AT GAMEGRAPH:" + gg.graphID);
                    g.m.ggMgr.comprobationResult = false;
                    exit(1);
                } else {

                    if (g.m.ggMgr.isNecesaryConditionsBasicCheck)
                        System.out.println(TAG + " BASIC CHECK: OK");
                    conds = newconds;
                    g.m.ggMgr.comprobationResult = true;
                }
            }
            this.conds = conds;

            //if(conds.contains("LOAD_DIALOG"))this.conds=parseConditions(conds.replaceAll("\\n", ""));
            //else this.conds = conds.replaceAll("\\s",""); // DELETE ALL BLANK SPACES; TABULATORS AND CARRIER RETURNS

            with_shooters = with_evaluators = with_randoms = with_moveprogs = with_timers = with_counters = with_idsCompare = with_sequences = with_choicers = false;
            _id_activation = new HashMap<String, Boolean>();
            _evaluators = new HashMap<String, String[]>();
            _shooters = new HashMap<String, Pair<Integer, String[]>>();
            randoms = new HashMap<String, String[]>();
            shooterTrees = new HashMap<String, SpecialSuperTree>();
            _directInstructions = new HashMap<String, String[]>();
            this.conditions = this.conds.split("%");

            if (conditions.length >= 2) {
                g.println("\n\nCONFIGURING CONDITIONS:");


                if (conditions[0].equalsIgnoreCase("AND")) { // CASE AND
                    and_compare = true;
                    or_compare = false;
                }
                if (conditions[0].equalsIgnoreCase("OR")) { // CASE OR
                    or_compare = true;
                    and_compare = false;
                }
                if (conditions[0].equalsIgnoreCase("none")) { // CASE none
                    or_compare = false;
                    and_compare = false;
                }
                if (and_compare || or_compare)
                    with_idsCompare = true;

                // READ ALL IDS AND VALIDATE THIS
                String[] ids;
                ids = conditions[1].split("@");
                int counterIds = 0;
                for (String s : ids) {
                    if (gg.isValid(s)) {
                        _id_activation.put(s, false);   // ADD VALID ID
                        counterIds++;
                    } else {                            // ADD INSTRUCTION
                        idInstructions.add(s);
                    }
                }
                if (counterIds == ids.length)
                    idInstructions.add("PASS");


                for (String ss : conditions) {                   // READ TIMERS AND RANDOMS AND COUNTERS AND EVALUATORS AND SHOOTERS AND DIRECTS
                    String[] s = ss.split("@");

                    String sType = s[0].split("#")[0];

                    if (sType.contains("TIMER")) {

                        if (s.length == 4) {
                            with_timers = true;
                            //String name,float init, float limit, int step,String preAndPostAction,boolean active
                            timers.put(s[0] + "#" + n.getID(), new com.mygdx.safe.MainGameGraph.Timer(g));

                            if(s[1].contains("VAR")) s[1]=single_var_replace(s[1]);

                            timers.get(s[0] + "#" + n.getID()).set(s[0] + "#" + n.getID(), //name
                                    Float.valueOf(s[1]),  //init=limit, endint at 0 step =-1
                                    s[2] + "@" + s[3] //  preAndPostAction
                            );
                        }

                        if (s.length == 7) {
                            with_timers = true;
                            //String name,float init, float limit, int step,String preAndPostAction,boolean active
                            timers.put(s[0], new com.mygdx.safe.MainGameGraph.Timer(g));
                            timers.get(s[0]).set(s[0] + "#" + n.getID(), //name
                                    Float.valueOf(s[1]),  //init
                                    Float.valueOf(s[2]), //limit
                                    Boolean.valueOf(s[3]), //step
                                    s[4] + "@" + s[5], //  preAndPostAction
                                    Boolean.getBoolean(s[6])
                            );
                        }

                    } else if (sType.contains("RANDOM")) { // PROCESSING ONE RANDOM INSTRUCTION, NOT MORE

                        if (s.length > 3) {
                            with_randoms = true;
                            randoms.put(s[0], s); // RANDOMS INSTRUCTIONS:  RANDOM_NAME@randomNumber@RND@RandomElection@ActionConsequence
                        }

                    } else if (sType.contains("COUNTER")) { // PROCESSING ALL COUNTERS EX: COUNTER==3@ActionConsequence1@ActionConsequence2

                        if (s[0].contains("==") && s[0].contains("SHOOTER") && s.length == 1) {
                            String[] countershooter = s[0].split("==");
                            counterShooterRelation.put(countershooter[0], countershooter[1]);
                        } else {

                            String counterComparator = "";
                            if (s.length > 1) {
                                with_counters = true;
                                String[] counterCompare;
                                if (s[0].contains("[]")) {
                                    counterComparator = "[]";
                                    counterCompare = s[0].split(counterComparator);
                                } else if (s[0].contains("<") && !(s[0].contains("<="))) {
                                    counterComparator = "<";
                                    counterCompare = s[0].split("<");

                                } else if (s[0].contains("<=")) {
                                    counterComparator = "<=";
                                    counterCompare = s[0].split("<=");

                                } else if (s[0].contains(">") && !(s[0].contains(">="))) {
                                    counterComparator = ">";
                                    counterCompare = s[0].split(">");

                                } else if (s[0].contains(">=")) {
                                    counterComparator = ">=";
                                    counterCompare = s[0].split(">=");

                                } else if (s[0].contains("==")) {
                                    counterComparator = "==";
                                    counterCompare = s[0].split("==");
                                } else {
                                    with_counters = false;
                                    g.println("Counter syntax error");
                                    counterCompare = null;
                                }

                                if (counterComparator.length() != 0) {
                                    String instructions = "";
                                    if (s.length > 1) {
                                        for (int i = 1; i < s.length; i++) {
                                            instructions += "@" + s[i];
                                        }
                                    } //EX: COUNTER   Pair<1,3>    []     @COUNTERINST1    @COUNTERINST2   @COUNTERINST3      //COMPARATOR FOR RANGE 1 TO 3
                                    //EX: COUNTER   Pair<1,1>    <=     @COUNTERINST1    @COUNTERINST2   @COUNTERINST3      //COMPARATOR FOR NUMBER 1
                                    //EX: COUNTER   Pair<2,2>    ==     @COUNTERINST1    @COUNTERINST2   @COUNTERINST3      //COMPARATOR FOR NUMBER 2

                                    if (counterInstructions.get(counterCompare[0]) == null) {
                                        counterInstructions.put(counterCompare[0], new HashMap<String, Array<String[]>>());
                                    }
                                    if (!(counterComparator.contains("[]"))) {

                                        int valueCompare = Integer.valueOf(counterCompare[1]);
                                        String key = valueCompare + "%" + valueCompare;

                                        if (counterInstructions.get(counterCompare[0]).get(key) == null) {
                                            counterInstructions.get(counterCompare[0]).put(key, new Array<String[]>());
                                            counterInstructions.get(counterCompare[0]).get(key).add((counterComparator + instructions).split("@"));
                                        } else {
                                            counterInstructions.get(counterCompare[0]).get(key).add((counterComparator + instructions).split("@"));
                                        }
                                    } else {

                                        int valueCompare1 = Integer.valueOf(counterCompare[1]);
                                        int valueCompare2 = Integer.valueOf(counterCompare[2]);
                                        String key = valueCompare1 + "%" + valueCompare2;

                                        if (counterInstructions.get(counterCompare[0]).get(key) == null) {
                                            counterInstructions.get(counterCompare[0]).put(key, new Array<String[]>());
                                            counterInstructions.get(counterCompare[0]).get(key).add((counterComparator + instructions).split("@"));
                                        } else {
                                            counterInstructions.get(counterCompare[0]).get(key).add((counterComparator + instructions).split("@"));
                                        }
                                    }
                                }
                            }
                        }
                    } else if (sType.contains("EVALUATOR")) { // PROCESSING ALL EVALUATOR SPECIAL RECEIVERS

                        with_evaluators = true;
                        _evaluators.put(s[0], s); //EVALUATIONS INSTRUCTIONS: EVALUATORNAME1@EVALUATION@TRUEINSTRUCTION@FALSEINSTRUCTION

                    } else if (sType.contains("SHOOTER")) { // PROCESSING ALL SHOOTER SPECIAL RECEIVERS: SHOOTERS ARE THE MOST IMPORTANT INSTRUCTION TYPE

                        with_shooters = true;
                        _shooters.put(s[0], new com.mygdx.safe.Pair(0, s)); //SHOOTER INSTRUCTIONS:
                        //SHOOTER_NAME@SHOOTER-TYPE@SHOOTER_INSTRUCTIONS1@SHOOTER_INSTRUCTIONS2 //
                        //SHOTERTYPE = SELECT#START, SELECT#STOP, NONE, BLOCKER, UNBLOCKER
                    } else if (sType.contains("DIRECT")) { // PROCESSING ALL DIRECT SPECIAL RECEIVERS
                        with_directInstructions = true;
                        _directInstructions.put(s[0], s); //DIRECT INSTRUCTIONS: DIRECT1@INSTRUCTION1@INSTRUCTION2@etc

                    } else if (sType.contains("SEQUENCE") && s.length > 2) {
                        with_sequences = true;
                        sequences.put(s[0] + "#" + n.getID(), new SequenceInstructions((s[0] + "#" + n.getID()), g.getPartialSplittedMessage(s, 1), n, g));
                    } else if (sType.contains("CHOICER") && s.length > 2) {

                        with_choicers = true;
                        _choicers.put(s[0], new Choicer(ss, g));

                    } else if (s[0].contains("MOVEPROG") && s.length == 4) { // MOVEPROGname@MoveProgOwner@MOVEPROG INSTRUCTIONS:

                        with_moveprogs = true;
                        String[] instructions = s[1].split("#");
                        Boolean[] crState = new Boolean[instructions.length];
                        Vector2 startPoint = null;
                        for (int i = 0; i < crState.length; i++) crState[i] = false;

                        com.mygdx.safe.Pair<String, Boolean> steps[] = new com.mygdx.safe.Pair[instructions.length];

                        for (int i = 0; i < instructions.length; i++) {
                            steps[i] = new com.mygdx.safe.Pair<String, Boolean>();
                            steps[i].setFirst(instructions[i]);
                            steps[i].setSecond(crState[i]);
                        }

                        if (!s[2].equalsIgnoreCase("none")) {
                            startPoint.x = Float.valueOf(s[2].split("_")[0]);
                            startPoint.y = Float.valueOf(s[2].split("_")[1]);
                        }

                        gg.getMoveprogs().put(s[0] + "#" + n.getID(), new ProgrammedMovement(0, s[0] + "#" + n.getID(), "", true, steps, startPoint, Boolean.valueOf(s[3]),g));


                    } else if (s[0].contains("VAR") && s[0].contains("=") && s.length == 2) {

                        with_vars = true;
                        String varstr[] = s[0].split("=");

                        if (s[1].equalsIgnoreCase("Integer"))
                            _varMap.put(varstr[0], new Pair<String, String>(varstr[1], s[1]));
                        else if (s[1].equalsIgnoreCase("Float"))
                            _varMap.put(varstr[0], new Pair<String, String>(varstr[1], s[1]));
                        else if (s[1].equalsIgnoreCase("String"))
                            _varMap.put(varstr[0], new Pair<String, String>(varstr[1], s[1]));

                    }

                }
            }

            setConditions = true;
            if(g.showingConditions) g.print("SHOWING:\n[\n" + show(timers, sequences) + "\n]\n\n");
            /*if(!with_shooters) {
                g.println(" FATAL ERROR!!!! HAVEN'NT SHOOTER IN NODE PROGRAM!!!!");
                exit(1);


                            g.printlns(TAG + "   " + counter + vCounter)
            }*/
        }
    }


    public String parseConditions(String conds) {

        String[] firstParse = conds.split("%");
        String c = "";
        boolean dialog = false;

        for (String s : firstParse) {
            if (s.contains("LOAD_DIALOG")) {
                String[] secondParse = s.split("@");
                for (String ss : secondParse) {
                    if (ss.contains("LOAD_DIALOG")) {
                        c += ss;
                    } else c += ss.replaceAll("\\s", "");
                    //System.out.println(TAG +" CONDITIONS DATA:["+conds+"]");

                    c += "@";
                }

                c = c.substring(0, c.length() - 1);
            } else {
                c += s.replaceAll("\\s", "");
            }

            c += "%";
        }


        g.println(TAG + " PARSE-CONDITIONS:  " + c.toString());


        return c;
    }


    public String show(HashMap<String, com.mygdx.safe.MainGameGraph.Timer> timers, HashMap<String, SequenceInstructions> sequence) {

        g.print("[" + n.getID() + "][" + n.get_nodeTypeAndName() + "]\n");
        g.println("CONDS: " + conds);
        g.println("CONDITIONS LENGTH: " + conditions.length);
        for (String s : conditions) {
            g.println("[CONDITION:" + s + "]");
        }
        String s = "";
        if (setConditions) {

            // SHOW SHOOTER INSTRUCTIONS
            if (with_shooters) {
                s += "\n[SHOOTERS]\n";
                for (com.mygdx.safe.Pair<Integer, String[]> shooterp : _shooters.values()) {
                    s += "\n[SHOOTER INSTRUCTION:]\n";
                    for (int j = 0; j < shooterp.getSecond().length; j++) {
                        s += "[" + shooterp.getSecond()[j] + "] ";
                    }
                    s += "\n";

                    g.println(TAG + "   " + shooterp.getFirst());
                }
            }
            // SHOW DIRECT INSTRUCTIONS
            if (with_directInstructions) {
                s += "\n[DIRECT_INSTRUCTIONS]\n";
                for (String[] direct : _directInstructions.values()) {
                    s += "\n[DIRECT INSTRUCTION:]\n";
                    for (int j = 0; j < direct.length; j++) {
                        s += "[" + direct[j] + "] ";
                    }
                    s += "\n";
                }
            }
            // SHOW IDCOMPARE INSTRUCTIONS
            if (with_idsCompare) {
                s += "[AND COMPARE:" + and_compare + "][OR COMPARE:" + or_compare + "]\n";
                s += "[ID_ACTIVATORS:]\n";
                for (String key : _id_activation.keySet()) {
                    s += "[ID:" + key + "]:[" + _id_activation.get(key).toString() + "]\n";
                }
                s += "[ID_INSTRUCTIONS:]\n";
                s += idInstructions.toString("\n");
            }
            // SHOW TIMER INSTRUCTIONS
            if (with_timers) {
                s += "\n[TIMERS:]\n";
                for (com.mygdx.safe.MainGameGraph.Timer t : timers.values()) {
                    if (t.getName().contains("#" + n.getID())) s += t.show();
                }
            }
            // SHOW RANDOM INSTRUCTIONS
            if (with_randoms) {
                s += "\n[RANDOM:]\n";
                s += "[RANDOM INSTRUCTIONS:]\n";
                for (String[] random : randoms.values()) {
                    for (int j = 0; j < random.length; j++) {
                        s += "[" + random[j] + "] ";
                    }
                    s += "\n";
                }
            }
            // SHOW COUNTER INSTRUCTIONS
            if (with_counters) {
                s += "\n[COUNTERS:]\n";

                if (!counterShooterRelation.isEmpty()) {
                    s += "\n[COUNTER-SHOOTER TABLE RELATION:]\n";
                    for (String zzz : counterShooterRelation.keySet()) {
                        s += zzz + " --> " + counterShooterRelation.get(zzz) + "\n";
                    }
                    s += "\n";
                }

                for (String countername : counterInstructions.keySet()) {
                    s += "NAME= [" + countername + "] : [COUNTER INSTRUCTIONS:]\n";
                    for (String ss : counterInstructions.get(countername).keySet()) {
                        s += " [" + ss + "] ,";
                        for (int j = 0; j < counterInstructions.get(countername).get(ss).size; j++) { //ARRAY
                            s += "\n        ";
                            for (int k = 0; k < counterInstructions.get(countername).get(ss).get(j).length; k++) // STRING[]
                                s += "\t[" + counterInstructions.get(countername).get(ss).get(j)[k] + "] ";
                        }
                        s += "\n";
                    }
                }
            }
            // SHOW EVALUATOR INSTRUCTIONS
            if (with_evaluators) {
                s += "\n[EVALUATORS]\n";
                for (String[] evaluator : _evaluators.values()) {
                    s += "\n[EVALUATOR INSTRUCTION:]\n";
                    for (int j = 0; j < evaluator.length; j++) {
                        s += "[" + evaluator[j] + "] ";
                    }
                    s += "\n";
                }
            }
            // SHOW WITH VARS INSTRUCTIONS ASIGNMENTS
            if (with_vars) {
                s += "\n[VARS:]\n";
                for (String svar : _varMap.keySet()) {
                    s += "[" + svar + "] == VALUE->>[" + _varMap.get(svar).getFirst() + "] TYPE->>[" + _varMap.get(svar).getSecond() + "]\n";
                }
            }
            // SHOW MOVE_PROGS INSTRUCTIONS
            if (with_moveprogs) {
                s += "\n[MOVEPROGS:]\n";
                for (String prog : _moveprogs.keySet()) {
                    s += "NAME=[" + prog + "]\n";
                    s += "ENTITY-OWNER" + _moveprogs.get(prog).getEntityOwner() + "]\n";
                    s += "ROUTECRASH STATE: [" + (_moveprogs.get(prog).getRouteCrashState() ? "ENABLE" : "DISABLE") + "]\n";

                    for (int i = 0; i < _moveprogs.get(prog).getMoveProg_Steps().length; i++) {
                        s += "[" + _moveprogs.get(prog).getMoveProg_Steps()[i].getFirst() + " ---- " + _moveprogs.get(prog).getMoveProg_Steps()[i].getSecond() + " ]";

                    }
                    s += "\n";
                }
                s += "\n";
            }

            // SHOW SEQUENCE INSTRUCTIONS
            if (with_sequences) {
                s += "\n[SEQUENCE:]\n";
                for (SequenceInstructions seq : sequence.values()) {
                    if (seq.getName().contains("#" + n.getID())) s += seq.show();
                }
            }

            if (with_choicers) {
                s += "\n[CHOICERS:]\n";
                for (Choicer ch : _choicers.values()) {
                    s += "  NAME: " + ch.getName() + " \n";
                    s += "  INSTRUCTION: " + ch.getInstruction() + "  \n";
                    s += "  CHOICE INSTRUCTIONS: \n";

                    for (String choiceInstruction : ch.getInstructionChoices()) {
                        s += " ---- " + choiceInstruction + "  ----\n";
                    }
                }
            }


            if (s.length() == 0)
                s = "Configured, and none instruction";

        } else {
            s = "Not configured";
        }

        return s;
    }

    public String sintaxBasicChecker(String conds, String nodeId, boolean isNecesaryCheck) {

        boolean check = true;
        String returncad = "";
        String[] condsEndlineChecker = conds.split("\n");
        Array<String> condsArray = new Array<String>();
        String newconds = "";

        if (isNecesaryCheck)
            g.println("\n\n\n" + TAG + " SINTAX BASIC CHECKER AT NODE: [" + nodeId + "]");

        // DIVIDE INSTRUCTION LINE TO LINE, WITH ('\n') SPLITTER
        for (int i = 0; i < condsEndlineChecker.length; i++) {
            // TRIMS BLANK SPACES, AT (INIT AND END)
            condsEndlineChecker[i] = condsEndlineChecker[i].trim();
            if (condsEndlineChecker[i].length() != 0) {
                // OLNY ADDS THE NOT BLANK LINES (SIZE 0)
                // AND IGNORES THE LINES WITH //
                if (condsEndlineChecker[i].contains("//")) {
                    if (isNecesaryCheck) g.println(TAG + " // NOT ADDING COMMENT LINE");
                    String[] eval = condsEndlineChecker[i].split("//");

                    if (!(condsEndlineChecker[i].equalsIgnoreCase("//")) && eval[0].trim().length() > 0) {
                        if (isNecesaryCheck)
                            g.println(TAG + "ADDING TRIM COMMENT LINE:[" + eval[0].trim() + "]");
                        condsArray.add(eval[0].trim());
                    }

                } else {
                    condsArray.add(condsEndlineChecker[i]);
                }
            }

        }

        if (isNecesaryCheck) g.println("\n\n" + TAG + " END OF CHECK BLANKS AND COMMENTS\n\n");
        String uck = "";
        for (String sss : condsArray) {
            if (isNecesaryCheck)
                g.println(TAG + " " + condsArray.indexOf(sss, false) + ":[" + sss + "]-[" + sss.length() + "]");
            if (!isNecesaryCheck) uck += sss;
        }

        if (isNecesaryCheck) {

            System.out.println(TAG + " CHECK NODE:" + nodeId);
            //CHECKING ALL FINAL LINES

            for (int i = 0; i < condsArray.size; i++) {
                String sss = condsArray.get(i);

                char end = sss.charAt(sss.length() - 1);


                if (i == condsArray.size - 1) {

                    if (end == '@' || end == '#' || end == '-') {
                        System.out.println("\n"+TAG + "[" + end + "] SYNTAX END CHARACTER ERROR:[@,#,-] AT LINE[" + i + "] :\n\n" + sss);
                        check = false;
                    } else {
                        newconds += sss;
                    }
                } else {
                    if (!(end == '%' || end == '@' || end == '#' || end == '-')) {

                        System.out.println("\n"+TAG + "[" + end + "] SYNTAX END CHARACTER ERROR:[%,@,#,-] AT LINE[" + i + "] :\n\n" + sss);
                        check = false;
                    } else {
                        newconds += sss;
                    }
                }

            }
            if (check) {

                conds = newconds;

                if (conds.contains("LOAD_DIALOG") || conds.contains("SET_TEXT") || conds.contains("GPRINT") || conds.contains("VAR"))
                    conds = parseConditions(conds.replaceAll("\\n", ""));
                else conds = conds.replaceAll("\\s", "");

                if (!conds.equalsIgnoreCase(newconds) && !((conds.contains("LOAD_DIALOG") || conds.contains("SET_TEXT") || conds.contains("GPRINT") || conds.contains("VAR")))) {
                    System.out.println(TAG + "CONDS\n" + conds);
                    System.out.println(TAG + "NEWCONDS\n" + newconds);
                    System.out.println("\n"+TAG + " ***************** DIFERENCE!!!!\n\n");
                    check = false;
                }

                if (check) {

                    // CHECKING MALFORMED (FIRST PART) INSTRUCTIONS
                    g.println("******************************************************************");
                    g.println("*                                                                *");
                    g.println("*             CHECKING MALFORMED (FIRST PART) INSTRUCTIONS       *");
                    g.println("*                                                                *");
                    g.println("******************************************************************");
                    String[] conds1split = conds.split("%");
                    String[] firsInst = firsinstructionNames.split("%");
                    String error = "";

                    Array<String[]> conds2split = new Array<String[]>();
                    for (int i = 0; i < conds1split.length; i++) {
                        conds2split.add(conds1split[i].split("@"));
                    }

                    for (String[] s : conds2split) {
                        boolean checkfirstInst = false;
                        for (String ss : firsInst) {
                            if (s[0].split("#")[0].split("=")[0].contains(ss)) {
                                //EVAUATE CHAR x CHAR
                                com.mygdx.safe.Pair<Boolean,String> p= evaluateCharXChar(ss, s[0]);
                                error+=p.getSecond();
                                if (!p.getFirst()) // SECONDCHECK
                                    checkfirstInst = false;
                                else {
                                    checkfirstInst = true;
                                    g.println(TAG + " CHECKING TRUED: " + s[0] + " WITH: " + ss);

                                }
                            }
                        }
                        check = checkfirstInst;
                        if (!check) {
                            System.out.println("\n"+ TAG + " ************ ERROR!!! " + s[0] + " IS: [" + error + "] MALFORMED INSTRUCTION ***********\n");
                            break;
                            }

                    }
                    // THIRD CHECK: REPETITION FIRST INSTRUCTION CHECK
                    if(check) {
                        g.println("******************************************************************");
                        g.println("*                                                                *");
                        g.println("*                   ANTI-REPETITION CHECKER                      *");
                        g.println("*                                                                *");
                        g.println("******************************************************************");


                    HashMap<String, Integer> comprobation = new HashMap<String, Integer>();
                    HashMap<String, Integer> comprobation2 = new HashMap<String, Integer>();
                    String[] thirdcomp = thirdComprobationNames.split("%");

                        for (String[] s : conds2split) {
                            g.print("\n"+TAG +" ANTI-REPETITION COMPROBATION:["+s[0]+"] ");
                            for (int i = 0; i < thirdcomp.length; i++) {
                                if (s[0].split("=")[0].contains(thirdcomp[i])) {
                                    g.print(" CHECK:["+s[0]+"]:[" +thirdcomp[i]+"]");

                                    if (comprobation.get(s[0]) == null) {
                                        comprobation.put(s[0], 1);
                                        g.print( " ::: [" + s[0] +"]" );
                                    } else {
                                        check = false;
                                        System.out.println("\n"+TAG + " *********** CHECK ERROR!!!!!\n\n" );

                                        break;
                                    }

                                }
                            }

                            if (!check) {
                                System.out.println("\n"+TAG + " ************ ERROR!!! [" + s[0] + "] REPEATED INSTRUCTION ***********\n\n");

                                break;
                            }
                            g.m.ggMgr.fourthComprobation.put(nodeId,comprobation);

                        }

                        if(check){

                            g.println("");
                            g.println("******************************************************************");
                            g.println("*                                                                *");
                            g.println("*                NODE FLUX CHECKER                               *");
                            g.println("*                                                                *");
                            g.println("******************************************************************");
                        }
                        for(String[] s4:conds2split)
                        {
                            if(check){

                                if (s4.length > 1) {
                                    g.print("\n" + TAG + " NODE-FLUX COMPROBATION:");
                                    for (String s5 : s4) {
                                        g.print("[" + s5 + "]");
                                    }
                                    g.print("\n");

                                    for (int j = 1; j < s4.length; j++) {

                                        for (int i = 0; i < thirdcomp.length; i++) {
                                            if (check) {
                                                String specialcheck="";
                                                if (!(s4[j].contains("#")) && s4[j].contains(thirdcomp[i])) {
                                                    g.print("::::[" + s4[j] + "]");

                                                    if (comprobation.get(s4[j]) == null) {

                                                        boolean check1 = false;

                                                        for (String s6 : comprobation.keySet()) {
                                                            if((s6.contains(s4[j]))) specialcheck=s6;
                                                            check1 = (check1 || ( s6.contains(s4[j]) && (s6.contains("COUNTER")) ) );
                                                        }
                                                        if (!check1) {

                                                            check = false;
                                                            g.println("\n\n[" + nodeId + "]" + comprobation.keySet().toString() + "\n\n");
                                                            System.out.println("\n" + TAG + " ************ ERROR!!! [" + s4[j] + "] ABSENT DEFINITION  ***********\n\n");
                                                            break;

                                                        } else {

                                                            g.print("[OOKK]   ");
                                                            comprobation2.put(specialcheck,1);
                                                        }

                                                    } else {
                                                        g.print("[OK]   ");
                                                        comprobation2.put(s4[j],1);
                                                    }

                                                }
                                            }
                                        }
                                        g.print("\n");
                                    }
                                }
                            }
                        }
                        if(check){
                            // EXTRA DEFINITION CHECK

                            for(String scomp:comprobation.keySet()){
                                if(!scomp.contains("SHOOTER") && !scomp.contains("COUNTER")) {
                                    if (comprobation2.get(scomp) == null) {
                                        check = false;
                                        g.println("\n\n[" + nodeId + "]" + comprobation.keySet().toString() + "\n\n");
                                        System.out.println("\n" + TAG + " ************ ERROR!!! [" + scomp + "] EXTRA (NOT RELATED) DEFINITION  ***********\n\n");
                                        break;
                                    }
                                }
                            }
                        }

                    }

                    if(check){
                        // SINTAX && LENGHT VERIFICATION
                        for(String[] s8:conds2split){
                            String instruction=s8[0];

                            if(instruction.contains("SEQUENCE")){
                                g.print(TAG+ " SEQUENCE LENGHT COMPROBATION: ["+instruction+"]");
                                if(s8.length<3){
                                    check=false;
                                    System.out.println("\n" + TAG + " ************ ERROR!!! ["+instruction+"]:" + s8.length + " INCORRECT LENGHT  ***********\n\n");
                                    break;
                                }
                                g.print("\n");
                            }

                            if(instruction.contains("EVALUATOR")){
                                g.print(TAG+ " EVALUATOR LENGHT COMPROBATION: ["+instruction+"]");
                                if(s8.length!=4){
                                    check=false;
                                    System.out.println("\n" + TAG + " ************ ERROR!!! ["+instruction+"]:" + s8.length + " INCORRECT LENGHT  ***********\n\n");
                                    break;
                                }
                                g.print("\n");
                            }

                            if(instruction.contains("TIMER")){
                                g.print(TAG+ " TIMER LENGHT COMPROBATION: ["+instruction+"]");
                                if(s8.length!=4 && s8.length!=7){
                                    check=false;
                                    System.out.println("\n" + TAG + " ************ ERROR!!! ["+instruction+"]:" + s8.length + " INCORRECT LENGHT  ***********\n\n");
                                    break;
                                }
                                g.print("\n");
                            }

                             for(int j=0;j<s8.length;j++){
                                 if(s8[j].contains("GO#") || s8[j].contains("LOAD#")){
                                     g.print(TAG +" LOAD/GO COMPROBATION:["+s8[j]+"]");
                                     String id="";
                                     if(s8[j].contains("GO#")){
                                         id=s8[j].split("GO#")[1].split("#")[0];

                                     }
                                     if(s8[j].contains("LOAD#")){
                                         id=s8[j].split("LOAD#")[1].split("#")[0];
                                     }
                                     if(!(id.contains("VAR")) && gameGraphCondition.getNodeById(id)==null){
                                         System.out.println(TAG + " ************ ERROR!!! GO ["+id+"] NOT VALID ID_GO DESTINATION");
                                         check=false;
                                         break;
                                     }else{g.print("[OK]\n");}
                                 }
                             }
                             if(!check) break;
                        }

                    }
                }

            }




            if (!check) {
                return null;
            } else {

                returncad = newconds;
            }
        } else {
            returncad = uck;
        }

        return returncad;

    }

    public com.mygdx.safe.Pair<Boolean,String> evaluateCharXChar(String ss, String s0) {

        String error="";
        boolean secondCheck=true;
        for(int j = 0; j<ss.length();j++) {
            if (!(ss.toCharArray()[j] == s0.split("%")[0].toCharArray()[j])) {
                    secondCheck = false;
                    error = ss;
                }
        }
        return new com.mygdx.safe.Pair<Boolean,String>(secondCheck,error);

    }

    public String single_var_replace(String instruction) {
        String nps = "";
        if (instruction.contains("VAR") &&
                        !(instruction.contains("SET_TEXTACTOR")) &&
                        !(instruction.contains("=")) &&
                        !(instruction.contains("<")) &&
                        !(instruction.contains(">"))) {

                    g.println(TAG + " **** [ SINGLE VAR-REPLACING ] **** [" + instruction + "] **** ["+ _varMap.get(instruction).getFirst()+"]");
                    nps = _varMap.get(instruction).getFirst();
                }
        return nps;
    }
}
