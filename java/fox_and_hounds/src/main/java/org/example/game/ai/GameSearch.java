/** @author Mark Watson
 * https://github.com/mark-watson/Java-AI-Book-Code/blob/master/search/src/main/java/search/game/GameSearch.java
 * under Apache License 2.0
 * modificated slightly by Sándor Vályi, 2021; by adding two parameters to createMove() method
 */
package org.example.game.ai;

import java.util.*;

public abstract class GameSearch {

    public static final boolean DEBUG = false;
    public static float INFINITY = 1000000.0f;
    /*
     * Note: the abstract Position also needs to be
     *       subclassed to write a new game program.
     */
    /*
     * Note: the abstract class Move also needs to be subclassed.
     *
     */

    public static boolean PROGRAM = false;
    public static boolean HUMAN = true;

    /**
     *  Notes:  PROGRAM false -1,  HUMAN true 1
     */

    public abstract boolean drawnPosition(Position p);
    public abstract boolean wonPosition(Position p, boolean player);
    public abstract float positionEvaluation(Position p, boolean player);
    public abstract void printPosition(Position p);
    public abstract Position [] possibleMoves(Position p, boolean player);
    public abstract Position makeMove(Position p, boolean player, Move move);
    public abstract boolean reachedMaxDepth(Position p, int depth);
    public abstract Move createMove(Position p, boolean player);


    public Vector alphaBeta(int depth, Position p, boolean player) {
        Vector v = alphaBetaHelper(depth, p, player, INFINITY, -INFINITY);
        return v;
    }

    protected Vector alphaBetaHelper(int depth, Position p,
                                     boolean player, float alpha, float beta) {
        if (reachedMaxDepth(p, depth)) {
            Vector v = new Vector(2);
            float value = positionEvaluation(p, player);
            v.addElement(new Float(value));
            v.addElement(null);
        if (GameSearch.DEBUG) System.out.println("VÉGMÉLYSÉG(depth:"+depth+","+p+","+"alfa:"+ alpha+", beta: "+beta+"player: "+
                 player+"), érték: "+value);
            return v;
        }
        Vector best = new Vector();
        Position [] moves = possibleMoves(p, player);
        for (int i=0; i<moves.length; i++) {
            Vector v2 = alphaBetaHelper(depth + 1, moves[i], !player, -beta, -alpha);
            float value = -((Float)v2.elementAt(0)).floatValue();
            if (value > beta) {
                beta = value;
                best = new Vector();
                best.addElement(moves[i]);
                Enumeration enum2 = v2.elements();
                enum2.nextElement();
                while (enum2.hasMoreElements()) {
                    Object o = enum2.nextElement();
                    if (o != null) best.addElement(o);
                }
            }
            if (beta >= alpha) {
                break;
            }
        }
        Vector v3 = new Vector();
        v3.addElement(new Float(beta));
        Enumeration enum2 = best.elements();
        while (enum2.hasMoreElements()) {
            v3.addElement(enum2.nextElement());
        }
        if (GameSearch.DEBUG) System.out.println("KÖZTES(depth:"+depth+","+p+","+"alfa:"+ alpha+", beta: "+beta+"player: "+
                 player+"), érték: "+beta);

        return v3;
    }
    public void playGame(Position startingPosition, boolean humanPlayFirst) {
        if (humanPlayFirst == false) {
            Vector v = alphaBeta(0, startingPosition, PROGRAM);
            startingPosition = (Position)v.elementAt(1);
        }
        while (true) {
            printPosition(startingPosition);
            if (wonPosition(startingPosition, PROGRAM)) {
                System.out.println("Vesztettel");
                break;
            }
            if (wonPosition(startingPosition, HUMAN)) {
                System.out.println("Nyertel");
                break;
            }
            if (drawnPosition(startingPosition)) {
                System.out.println("Dontetlen");
                break;
            }
            System.out.println("Te lepsz:");
            Move move = createMove(startingPosition,HUMAN);
            startingPosition = makeMove(startingPosition, HUMAN, move);
            printPosition(startingPosition);
            Vector v = alphaBeta(0, startingPosition, PROGRAM);
            Enumeration enum2 = v.elements();
            if (DEBUG)
                while (enum2.hasMoreElements()) {
                    System.out.println(" ai: " + enum2.nextElement());
                }
            startingPosition = (Position)v.elementAt(1);
        }
    }
}
