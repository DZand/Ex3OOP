// 
// Decompiled by Procyon v0.5.36
// 

package Server;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.HashMap;

public class Users extends HashMap<Long, User>
{
    public static final String InputFile = "data/OOP_users.csv";
    private static final long serialVersionUID = 1L;
    
    public void initFromFile() {
        this.initFromFile("data/OOP_users.csv");
    }
    
    public void initFromFile(final String f) {
        try {
            final File file = new File(f);
            final BufferedReader br = new BufferedReader(new FileReader(file));
            br.readLine();
            String st;
            while ((st = br.readLine()) != null) {
                final User r = new User(st);
                super.put(r.getID(), r);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
