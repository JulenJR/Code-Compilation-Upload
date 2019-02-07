package com.mygdx.safe.IA;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.safe.MapManager;

/**
 * Created by Boris Escajadillo on 30/01/18.
 */

public class CollisionMatrix {

    private int mapx=0;
    private int mapy=0;
    private int[][] AllCollisionMatrix;
    private MapManager mpmgr=null;
    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    public CollisionMatrix(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g){
        this.g=g;

    }

    public void setMpmgr(MapManager mpmgr) {
        this.mpmgr = mpmgr;
        createMatrix();
        addMapCollisions();
    }

    /*public void setGameSystem(GameSystem gsys) {
        if (mpmgr != null) {
            for (GameEntity ge : gsys.getEntities()) {
                for (Rectangle r : ge.getPhysicsComponent().get_nBoundingBoxes().values()) {
                    addCollisionAtMatrix(r);
                }
            }
        }
    }*/

    public void createMatrix(){

        com.mygdx.safe.Map cmap=mpmgr.get_currentMap();
        mapx=cmap.MAP_WIDHT;
        mapy=cmap.MAP_HEIGHT;
        AllCollisionMatrix=new int[mapx][mapy];
        for(int i=0;i<mapx;i++){
            for (int j=0;j<mapy;j++){
                AllCollisionMatrix[i][j]=0;
            }
        }
    }

    public void addCollisionAtMatrix(Rectangle r ){

        int originx=(int) (r.getX());
        int originy=(int) (r.getY());

        for(int x=0;x<(int)(r.getWidth()+0.5f);x++){
            for(int y=0;y<(int)(r.getHeight()+0.5f);y++){
                AllCollisionMatrix[originx+x][originy+y]=1;
            }
        }
    }

    public void addMapCollisions(){
        com.mygdx.safe.Map cmap=mpmgr.get_currentMap();
        for(Rectangle r:cmap.getCollisionRectangles()){
            addCollisionAtMatrix(r);
        }
    }

    // RETURNS -1 IF THE DESTINY POSITION IS NOT EMPTY COLLISION
    //          0 AT SUCCESS
    public int getCalculatedRoute(Vector2 origin,Vector2 destiny){
        int originx=(int) origin.x;
        int originy=(int) origin.y;
        int destinyx=(int) destiny.x;
        int destinyy=(int) destiny.y;
        if(AllCollisionMatrix[destinyx][destinyy]!=0){
            return -1;
        }else{
            //INIT OF CALCULATED ROUTE AND TARGETS WITH "2" the ROUTE
        }

        return 0;
    }




}
