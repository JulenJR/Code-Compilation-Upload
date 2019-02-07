package com.mygdx.safe.IA;

import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.safe.Conversation.TriceptionStepGraph;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

/**
 * Created by Boris.InspiratGames on 25/07/17.
 */

public class Instruction {

    //TAG
    private static final String TAG = IAMasterControl.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //ID NUM
    int _num;

    //STRING
    String _id;
    String _action;
    String _fromEntity;
    String _toWithEntity;
    String _mode;
    String _until;

    //QUANTITY
    Vector2 _quantity;

    //PERIMETER
    Rectangle _perimeter;

    //TIME
    float _time;

    //TRICEPTION STEP GRAPH
    TriceptionStepGraph _dialogTriceptionStepGraph;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public Instruction(GenericMethodsInputProcessor g){
        this.g = g;

        _num=0;
        _id=_num+"";
        _action="none";
        _fromEntity="none";
        _toWithEntity="none";
        _perimeter=new Rectangle(0f,0f,0f,0f);
        _quantity=new Vector2(0f,0f);
        _time=0;
        _mode="none";
        _dialogTriceptionStepGraph =null;
        _until="none";
    }

    public Instruction(int n, String a, String fe, String te, Rectangle p, Vector2 q, float t, String m, TriceptionStepGraph d, String u, GenericMethodsInputProcessor g){
        this(g);
        setInstruction(n,a,fe,te,p,q,t,m,d,u);
    }

    public Instruction(Instruction i){
        this(i.g);
        setInstruction(i._num,i._action,i._fromEntity,i._toWithEntity,i._perimeter,i._quantity,i._time,i._mode,i._dialogTriceptionStepGraph,i._until);
    }

    /*_______________________________________________________________________________________________________________*/

    //PRINT INSTRUCTION
    public void printInstruction(){
        g.println(" _num              : "+ _num                      );
        g.println(" _id               : "+ _id                       );
        g.println(" _action           : "+ _action                   );
        g.println(" _fromEntity       : "+ _fromEntity               );
        g.println(" _toWithEntity     : "+ _toWithEntity             );
        g.println(" _perimeter        : "+ _perimeter                );
        g.println(" _quantity         : "+ _quantity                 );
        g.println(" _time             : "+ _time                     );
        g.println(" _mode             : "+ _mode                     );
        g.println(" _dialogTriceptionStepGraph  : "+ _dialogTriceptionStepGraph);
        g.println(" _until            : "+ _until                    );
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public Instruction getInstruction(){
        return this;
    }

    public int get_num() {
        return _num;
    }

    public String get_id() {
        return _id;
    }

    public String get_action() {
        return _action;
    }

    public String get_fromEntity() {
        return _fromEntity;
    }

    public String get_toWithEntity() {
        return _toWithEntity;
    }

    public String get_mode() {
        return _mode;
    }

    public String get_until() {
        return _until;
    }

    public Vector2 get_quantity() {
        return _quantity;
    }

    public Rectangle get_perimeter() {
        return _perimeter;
    }

    public float get_time() {
        return _time;
    }

    public TriceptionStepGraph get_dialogTriceptionStepGraph() {
        return _dialogTriceptionStepGraph;
    }

    //SETTERS

    public void setInstruction(int n, String a, String fe, String te, Rectangle p, Vector2 q, float t, String m, TriceptionStepGraph d, String u){
        this._num=n;
        this._id=this._num+"";
        this._action=a;
        this._fromEntity=fe;
        this._toWithEntity=te;
        this._perimeter.set(p);
        this._quantity.set(q);
        this._time=t;
        this._mode=m;
        this._dialogTriceptionStepGraph =new TriceptionStepGraph();
        this._dialogTriceptionStepGraph =d;
        this._until=u;
    }

    public void set_num(int _num) {
        this._num = _num;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_action(String _action) {
        this._action = _action;
    }

    public void set_fromEntity(String _fromEntity) {
        this._fromEntity = _fromEntity;
    }

    public void set_toWithEntity(String _toWithEntity) {
        this._toWithEntity = _toWithEntity;
    }

    public void set_mode(String _mode) {
        this._mode = _mode;
    }

    public void set_until(String _until) {
        this._until = _until;
    }

    public void set_quantity(Vector2 _quantity) {
        this._quantity = _quantity;
    }

    public void set_perimeter(Rectangle _perimeter) {
        this._perimeter = _perimeter;
    }

    public void set_time(float _time) {
        this._time = _time;
    }

    public void set_dialogTriceptionStepGraph(TriceptionStepGraph _dialogTriceptionStepGraph) {
        this._dialogTriceptionStepGraph = _dialogTriceptionStepGraph;
    }

}
