public class jaroDistance {

    public double apply(String first, String second) {
        first = first.replaceAll("\\s", "").trim();
        second = second.replaceAll("\\s", "").trim();
        System.out.println(first + " " + second);
        first = first.toLowerCase();
        second = second.toLowerCase();
        int s_len = first.length();
        int t_len = second.length();
        if (s_len == 0 && t_len == 0) return 1;
        int match_distance = Integer.max(s_len, t_len) / 2 - 1;
        boolean[] s_matches = new boolean[s_len];
        boolean[] t_matches = new boolean[t_len];
        int matches = 0;
        for (int i = 0; i < s_len; i++) {
            int start = Integer.max(0, i - match_distance);
            int end = Integer.min(i + match_distance + 1, t_len);
            for (int j = start; j < end; j++) {
                if (t_matches[j]) continue;
                if (first.charAt(i) != second.charAt(j)) continue;
                s_matches[i] = true;
                t_matches[j] = true;
                matches++;
                break;
            }
        }
        if (matches == 0) return 0;
        int transpositionsval = transpositions(first, second);
        double dist =
                (matches / ((double) first.length()) +
                        matches / ((double) second.length()) +
                        (matches - transpositionsval) / ((double) matches)) / 3.0;
        System.out.println(dist);
        double jaroWinkler = (dist + (0.05 * findLongestCommonPrefix(first, second) )) * 100;
        return jaroWinkler;
    }

    private int transpositions(String first, String second) {
        int transpositions = 0;
        for (int i = 0; i < Math.min(first.length(), second.length()); i++) {
            if (first.charAt(i) != second.charAt(i))
                transpositions++;
        }
        transpositions /= 2;
        System.out.println(transpositions);
        return transpositions;
    }

    private int findLongestCommonPrefix(String s1, String s2) {
        int prefixCount = 0;
        for (int i = 0; i < Math.min(s1.length(), s2.length()); i++) {
            if (s1.charAt(i) != s2.charAt(i))
                break;
            prefixCount++;
        }
        return prefixCount;
    }
}



