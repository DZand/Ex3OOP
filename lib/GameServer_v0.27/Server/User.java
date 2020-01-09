// 
// Decompiled by Procyon v0.5.36
// 

package Server;

import java.util.Date;
import java.io.Serializable;

public class User implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final long TIME_OUT = 2000L;
    private long _id;
    private String _first_name;
    private String _last_name;
    private String _email;
    private long _last_seen;
    
    public User(final long id, final String fn, final String ln, final String email) {
        this._id = id;
        this._first_name = fn;
        this._last_name = ln;
        this._email = email;
        this._last_seen = new Date().getTime();
    }
    
    public User(final String s) {
        final String[] w = s.split(",");
        this._id = Long.parseLong(w[2]);
        this._first_name = w[0];
        this._last_name = w[1];
        this._email = w[4];
        this._last_seen = new Date().getTime();
    }
    
    public long getID() {
        return this._id;
    }
    
    public long getLastSeen() {
        return this._last_seen;
    }
    
    public double timeSeensLastUsed() {
        final long now = new Date().getTime();
        final long dt = now - this._last_seen;
        return (double)dt;
    }
    
    public boolean update() {
        boolean ans = false;
        if (this.isActive()) {
            ans = true;
            this._last_seen = new Date().getTime();
        }
        return ans;
    }
    
    public boolean isActive() {
        return this.timeSeensLastUsed() < 2000.0;
    }
    
    public String getEmail() {
        return this._email;
    }
    
    public String getFirstName() {
        return this._first_name;
    }
    
    public String getlastName() {
        return this._last_name;
    }
    
    @Override
    public String toString() {
        return this._id + "," + this._first_name + "," + this._last_name + "," + this._email;
    }
}
