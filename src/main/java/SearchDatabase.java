import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchDatabase {
    static Trie.TrieNode root;
    private static java.sql.Connection Conn;
    private  static PreparedStatement PrepareStat ;

    public static void main(String args[]) throws SQLException {
        // Input keys (use only 'a' through 'z' and lower case)
        String output[] = {"Not present in trie", "Present in trie"};
        try {
            Conn = DBOperations.makeJDBCConnection();
            String getQueryStatement = "SELECT Name FROM phonedatabase limit 100";
            PrepareStat = Conn.prepareStatement(getQueryStatement);
            ResultSet rs = PrepareStat.executeQuery();
            while (rs.next()) {
                String databseName = rs.getString("Name");
                databseName = databseName.replaceAll("%20", "");
                databseName = databseName.replaceAll(" ", "");
                databseName = databseName.toLowerCase();
                System.out.println(databseName);
                Trie.insert(databseName);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        finally {
            Conn.close();
        }
            String websiteName = "apple iphone x plus";
            websiteName = websiteName.replaceAll("\\s", "");
            websiteName = websiteName.toLowerCase();
//        String val[]
            if (Trie.search(websiteName) == true)
                System.out.println("the --- " + output[1]);
            else System.out.println("the --- " + output[0]);
    }

}

class Trie {
    // Alphabet size (# of symbols)
    static final int ALPHABET_SIZE = 50;
    // trie node
    static class TrieNode
    {
        TrieNode[] children = new TrieNode[ALPHABET_SIZE];
        // isEndOfWord is true if the node represents
        // end of a word
        boolean isEndOfWord;
        TrieNode(){
            isEndOfWord = false;
            for (int i = 0; i < ALPHABET_SIZE; i++)
                children[i] = null;
        }
    }
    static TrieNode root=new Trie.TrieNode();
    // If not present, inserts key into trie
    // If the key is prefix of trie node,
    // just marks leaf node
    static void insert(String key)
    {
        int level;
        int length = key.length();
        int index=0;
        TrieNode pCrawl = root;
        for (level = 0; level < length; level++)
        {
            if(key.charAt(level)>='a')
                index = key.charAt(level) - 'a';
            else if(key.charAt(level)>='0' && key.charAt(level)<='9')
                index=key.charAt(level)-'0'+26;
            else if(key.charAt(level)>='!' && key.charAt(level)<='/')
                index=key.charAt(level)-'!'+35;
            else if(key.charAt(level)=='-')
                index=key.charAt(level)-'-'+49;

            if (pCrawl.children[index] == null) {
                pCrawl.children[index] = new TrieNode();
            }
            pCrawl = pCrawl.children[index];
        }
        // mark last node as leaf
        pCrawl.isEndOfWord = true;
    }
    // Returns true if key presents in trie, else false
    static boolean search(String key)
    {
        int level;
        int length = key.length();
        int index=0;
        TrieNode pCrawl = root;
        for (level = 0; level < length; level++)
        {
            if(key.charAt(level)>='a' && key.charAt(level)<='z')
                index = key.charAt(level) - 'a';
            else if(key.charAt(level)>='0' && key.charAt(level)<='9')
                index=key.charAt(level)-'0'+25;
            else if(key.charAt(level)>='!' && key.charAt(level)<='/')
                index=key.charAt(level)-'!'+35;
            else if(key.charAt(level)=='-')
                index=key.charAt(level)-'-'+49;

//            if (pCrawl.children[index] == null)
//                return false;

            pCrawl = pCrawl.children[index];
        }

        return (pCrawl != null && pCrawl.isEndOfWord);
    }

    // Driver

}