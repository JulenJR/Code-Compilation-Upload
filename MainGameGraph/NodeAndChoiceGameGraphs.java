package com.mygdx.safe.MainGameGraph;

import com.badlogic.gdx.utils.Array;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

import java.util.HashMap;

/**
 * Created by Boris.InspiratGames on 1/11/17.
 */

public class NodeAndChoiceGameGraphs {
    // SINTAX CONVENTION NAMEGRAPH:
    // LVL#numberOfLevel#numberOfSubLevel@LOOKSCREEN#lookScreenType("DARKER", "DARK", "STANDAR", "LIGHT")
    // EX: LVL#1#1@LOOKSCREEN#STANDAR
    String graphName;
    String rootId;
    Array<ToActionNode> nodes;
    Array<com.mygdx.safe.MainGameGraph.ActionChoice> actions;

    // conds: AND$ID_1@ID_2$TIMER_1@30@GUARD_00#changeToCircunscription#GUARD_00#1#2@GUARD_00#changeToCircunscription#GUARD_00#1#1$none
    // conds: AND_OR
    //            $ID_1@ID_2@ID_3@ActionConsequence         // cada ID es una condicion (ActionChoice) necesaria para activar el ToActionNode;
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
    //        $RANDOM_1@randomNumber@RandomElection@ActionConsequence            // Cada RANDOM_X contiene un numero de posibilidades, y la elegida para hacerlo true;
    //
    //
    // # <- Instruction Commands separator
    // $ <- Diferent type Instruction Separator
    // @ <- Same type instruction Separator

    public HashMap<String,ToActionNode> getHashMapNodes(GenericMethodsInputProcessor g){ // SETS ALL NODES
        HashMap<String,ToActionNode> hashmapnodes= new HashMap<String,ToActionNode> ();
        for(ToActionNode t: nodes){

            t.setToActionNode(g);
            hashmapnodes.put(t.getID(),t);
        }

        return hashmapnodes;
    }

    public Array<com.mygdx.safe.MainGameGraph.ActionChoice> getActions() {
        return actions;
    }
}
