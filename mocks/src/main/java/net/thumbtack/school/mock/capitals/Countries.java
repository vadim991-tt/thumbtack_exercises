package net.thumbtack.school.mock.capitals;

import net.thumbtack.school.mock.jdbc.JdbcUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Countries {


    public static List<String> downloadInformationAboutCountries() throws IOException, SQLException {
        List<String> capitals = getCapitalsFromDB();
        List<String> countriesList = new ArrayList<>();
        String toReturn;

        for (String elem : capitals) {
            String str = downloadPage(elem);
            toReturn = str.replace("{", "").replace("\"", "")
                    .replace("}", "")
                    .replace(":", ": ")
                    .replace(",", ", ");
            countriesList.add(toReturn);

        }
        createFile(countriesList);
        return countriesList;
    }


    public static String downloadPage(String pageName) throws IOException {
        String urlString = "http://restcountries.eu/rest/v2/capital/" + pageName;
        if (urlString.contains(" ")) {
            urlString = urlString.replace(" ", "%20");
        }

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();

        Pattern namePattern = Pattern.compile("\\{\"name\":\"[^\"]*\"");
        Pattern currencyPattern = Pattern.compile("\"currencies\":[^]]*]");

        StringBuilder sb = new StringBuilder();
        sb.append(pageName);
        sb.append("\n");

        try (InputStreamReader in = new InputStreamReader((InputStream) conn.getContent());
             BufferedReader buff = new BufferedReader(in)) {
            String line;
            while ((line = buff.readLine()) != null) {
                sb.append(extractStringFromLine(namePattern, line));
                sb.append("\n");
                sb.append(extractStringFromLine(currencyPattern, line));
            }
        } catch (FileNotFoundException ex) {
            sb.append("Country not found");
        }
        sb.append("\n");

        conn.disconnect();
        return sb.toString();
    }

    public static String extractStringFromLine(Pattern pattern, String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }


    public static List<String> getCapitalsFromDB() throws SQLException {
        JdbcUtils.createConnection();
        Connection con = JdbcUtils.getConnection();
        List<String> capitals = new ArrayList<>();

        String getCapitals = "SELECT * FROM CAPITALS;";
        try (PreparedStatement stmt = con.prepareStatement(getCapitals)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                capitals.add(rs.getString(2));
            }
        }
        JdbcUtils.closeConnection();
        return capitals;
    }

    public static void createFile(List<String> countriesList) throws IOException {
        File file = new File("countries.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String elem : countriesList) {
                bw.write(elem);
                bw.newLine();
            }
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        downloadInformationAboutCountries();
    }

}
