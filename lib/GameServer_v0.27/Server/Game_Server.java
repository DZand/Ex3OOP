// 
// Decompiled by Procyon v0.5.36
// 

package Server;

import java.util.List;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import oop_utils.OOP_Point3D;
import oop_dataStructure.oop_edge_data;
import java.util.Date;
import java.io.File;
import oop_dataStructure.OOP_DGraph;
import org.json.JSONObject;
import java.util.Random;
import java.util.ArrayList;
import oop_dataStructure.oop_graph;

public class Game_Server implements game_service
{
    public static final long SEED = 3331L;
    private static final String LOG1 = "data/1.obj";
    private static final String LOG2 = "data/2.obj";
    private Users _users;
    private oop_graph _graph;
    private ArrayList<fruits> _fruits;
    private ArrayList<robot> _robot;
    private ArrayList<String> _log;
    private String _curr_log;
    private User _user;
    private long _time_out;
    private long _start_time;
    private String data;
    private boolean _running;
    private double _grade;
    private int _robots_number;
    private int _number_of_moves;
    private int _fruits_number;
    private long _seed;
    private Random _rand;
    
    private Game_Server() {
        this._running = false;
        this._seed = 3331L;
        this._users = new Users();
        final String f = "data/OOP_users.csv";
        this._users.initFromFile(f);
    }
    
    public static game_service getServer(final int g) {
        game_service ans = null;
        if (g >= 0 && g < 24) {
            final int gr = g / 4;
            int ro = 1;
            final int fr = 1 + g % 6;
            if (g > 10) {
                ro = 1 + g % 3;
            }
            long time = 30000L;
            if (g % 2 == 1) {
                time *= 2L;
            }
            ans = new Game_Server("data/A" + gr, ro, fr, time, 3331L, g);
        }
        if (g == 9125) {
            final int gr = 5;
            final int ro = 10;
            final int fr = 20;
            final long time = 120000L;
            ans = new Game_Server("data/A" + gr, ro, fr, time, 3331L, g);
        }
        return ans;
    }
    
    @Override
    public String toString() {
        return this.toJSON();
    }
    
    public String toJSON1() {
        final String ans = "{\"GameServer\":{\"graph\":\"" + this.data + "\"," + "\"fruits\":" + this._fruits_number + "," + "\"grade\":" + this._grade + "," + "\"moves\":" + this._number_of_moves + "," + "\"robots\":" + this._robots_number + "}" + "}";
        return ans;
    }
    
    public String toJSON() {
        String ans = null;
        final JSONObject res = new JSONObject();
        final JSONObject data = new JSONObject();
        try {
            data.put("graph", (Object)this.data);
            data.put("fruits", this._fruits_number);
            data.put("grade", this._grade);
            data.put("moves", this._number_of_moves);
            data.put("robots", this._robots_number);
            res.put("GameServer", (Object)data);
            ans = res.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }
    
    private Game_Server(final String file_graph, final int robot_num, final int fruit_num, final long time, final long seed, final int g) {
        this._running = false;
        this._seed = 3331L;
        this.data = file_graph;
        this._graph = new OOP_DGraph(file_graph);
        this._grade = 0.0;
        this._robots_number = robot_num;
        this._fruits_number = fruit_num;
        this._seed = seed;
        this._rand = new Random(this._seed);
        this._time_out = time;
        this._start_time = -1L;
        this._curr_log = new StringBuilder().append(g).toString();
        this._fruits = new ArrayList<fruits>();
        for (int i = 0; i < this._fruits_number; ++i) {
            this._fruits.add(this.randomFruit());
        }
        this._robot = new ArrayList<robot>();
        this.initLog();
    }
    
    private void initLog() {
        final File f1 = new File("data/1.obj");
        final File f2 = new File("data/2.obj");
        boolean aa = false;
        if (f1.exists()) {
            aa = this.load("data/1.obj");
            this.save("data/2.obj");
        }
        if (!aa && f2.exists()) {
            aa = this.load("data/2.obj");
            this.save("data/1.obj");
        }
        if (!aa) {
            final long now = new Date().getTime();
            final String err = now + ",ERR: no log, init new log at: ";
            (this._log = new ArrayList<String>()).add(err);
            this.save("data/1.obj");
            this.save("data/2.obj");
        }
    }
    
    @Override
    public boolean addRobot(final int start_node) {
        boolean ans = false;
        if (this._robot.size() < this._robots_number && this._graph.getNode(start_node) != null) {
            final RobotG r = new RobotG(this._graph, start_node);
            this._robot.add(r);
            ans = true;
        }
        return ans;
    }
    
    private fruits randomFruit() {
        int ind = -1;
        for (int vs = this._graph.nodeSize(); this._graph.getNode(ind) == null; ind = (int)(this.random() * vs)) {}
        final Iterator<oop_edge_data> itr = this._graph.getE(ind).iterator();
        final oop_edge_data e = itr.next();
        double r = this.random() * 0.6;
        r += 0.2;
        final OOP_Point3D p = this._graph.getNode(e.getSrc()).getLocation();
        final OOP_Point3D d = this._graph.getNode(e.getDest()).getLocation();
        final double dx = r * (d.x() - p.x());
        final double dy = r * (d.y() - p.y());
        final OOP_Point3D pos = new OOP_Point3D(p.x() + dx, p.y() + dy, p.z());
        final int v = (int)(5.0 + this.random() * 11.0);
        return new Fruit(v, pos, e);
    }
    
    public void resetSeed() {
        this.resetSeed(this._seed);
    }
    
    public void resetSeed(final long seed) {
        this._seed = seed;
        this._rand = new Random(this._seed);
    }
    
    double random() {
        return this._rand.nextDouble();
    }
    
    public boolean save(final String file_name) {
        boolean ans = false;
        try {
            final FileOutputStream fout = new FileOutputStream(file_name);
            final ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(this._log);
            ans = true;
            oos.close();
            fout.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return ans;
    }
    
    public boolean load(final String file_name) {
        boolean ans = false;
        try {
            final FileInputStream streamIn = new FileInputStream(file_name);
            final ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
            this._log = (ArrayList<String>)objectinputstream.readObject();
            ans = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }
    
    @Override
    public String getGraph() {
        return this._graph.toString();
    }
    
    @Override
    public List<String> getFruits() {
        final ArrayList<String> ans = new ArrayList<String>();
        for (int i = 0; i < this._fruits.size(); ++i) {
            ans.add(this._fruits.get(i).toString());
        }
        return ans;
    }
    
    @Override
    public List<String> getRobots() {
        final ArrayList<String> ans = new ArrayList<String>();
        for (int i = 0; i < this._robot.size(); ++i) {
            ans.add(this._robot.get(i).toString());
        }
        return ans;
    }
    
    @Override
    public long startGame() {
        long ans = -1L;
        if (this._robots_number == this._robot.size() && !this.isRunning()) {
            this._running = true;
            this._start_time = new Date().getTime();
            ans = this._start_time;
            this._grade = 0.0;
            this._curr_log = ans + "," + this._curr_log;
        }
        return ans;
    }
    
    @Override
    public long stopGame() {
        this._curr_log = String.valueOf(this._curr_log) + "," + this._number_of_moves + "," + this._grade;
        this._log.add(this._curr_log);
        this.save("data/1.obj");
        this.save("data/2.obj");
        this._running = false;
        this._start_time = -1L;
        return new Date().getTime();
    }
    
    @Override
    public List<String> move() {
        if (this.isRunning()) {
            ++this._number_of_moves;
            double g = 0.0;
            for (int i = 0; i < this._robot.size(); ++i) {
                final robot c = this._robot.get(i);
                c.move();
                this.play(c);
                g += c.getMoney();
            }
            this._grade = g;
            return this.getRobots();
        }
        return null;
    }
    
    private void play(final robot r) {
        double dx = 0.1;
        dx /= 50.0;
        int i = 0;
        int rf = 0;
        while (i < this._fruits.size()) {
            final fruits f = this._fruits.get(i);
            final double v = f.grap(r, dx);
            if (v > 0.0) {
                final double dee = v;
                r.addMoney(dee);
                this._fruits.remove(i);
                ++rf;
            }
            else {
                ++i;
            }
        }
        while (rf > 0) {
            this._fruits.add(this.randomFruit());
            --rf;
        }
    }
    
    @Override
    public long chooseNextEdge(final int id, final int next_node) {
        long ans = -1L;
        if (!this.isRunning()) {
            return ans;
        }
        for (int i = 0; i < this._robot.size(); ++i) {
            final robot c = this._robot.get(i);
            if (c.getID() == id && !c.isMoving()) {
                final oop_edge_data e = this._graph.getEdge(c.getSrcNode(), next_node);
                if (e != null) {
                    c.setNextNode(next_node);
                    ans = new Date().getTime();
                }
            }
        }
        return ans;
    }
    
    @Override
    public boolean isRunning() {
        final boolean c = this._running;
        final long tt = this.timeToEnd();
        if (!c || !this._running) {}
        return this._running;
    }
    
    @Override
    public long timeToEnd() {
        if (!this._running) {
            return -1L;
        }
        final long now = new Date().getTime();
        final long dt = now - this._start_time;
        long tt = this._time_out - dt;
        if (tt <= 0L) {
            final long n = this.stopGame();
            tt = -1L;
        }
        return tt;
    }
}
