package net.thumbtack.school.mock;

import net.thumbtack.school.mock.capitals.Countries;
import net.thumbtack.school.mock.jdbc.JdbcUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;
import java.util.List;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest(JdbcUtils.class)
public class TestFakeDB {

    private static final String createTablesSQL =
            "CREATE TABLE capitals(\n" +
                    "id INT NOT NULL AUTO_INCREMENT,\n" +
                    "capitalname VARCHAR(50) NOT NULL,\n" +
                    "UNIQUE KEY capitalname (capitalname),\n" +
                    "PRIMARY KEY (id)\n" +
                    ");";

    private static final JdbcDataSource ds = new JdbcDataSource();
    private static Connection connection;

    @BeforeClass()
    public static void setUp() throws SQLException {
        ds.setURL("jdbc:h2:mem:~/capitals");
        ds.setUser("sa");
        ds.setPassword("sa");
        connection = ds.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(createTablesSQL)) {
            stmt.executeUpdate();
        }
    }

    @Before()
    public void clearDatabase() {
        PowerMockito.mockStatic(JdbcUtils.class);
        when(JdbcUtils.getConnection()).thenReturn(connection);

    }


    // Нашёл минус базы H2 базы данных - не воспринимает " как начало строки и ' как символ
    // (как бы я его не экранировал, программа воспринимает его как специальный символ и выдаёт ошибку).
    @Test
    public void insertCountries() throws SQLException {
        String badQuery = "INSERT INTO Capitals VALUES (null, \"Abidjan\"), (null, \"Yamoussoukro\"), (null, \"Abu Dhabi\"), (null, \"Abuja\"), (null, \"Accra\"), (null, \"Adamstown\"), (null, \"Addis Ababa\"), (null, \"Aden\"), (null, \"Sana'a\"), (null, \"Algiers\"), (null, \"Alofi\"), (null, \"Amman\"), (null, \"Amsterdam\"), (null, \"The Hague\"), (null, \"Andorra la Vella\"), (null, \"Ankara\"), (null, \"Antananarivo\"), (null, \"Apia\"), (null, \"Ashgabat\"), (null, \"Asmara\"), (null, \"Asunción\"), (null, \"Athens\"), (null, \"Avarua\"), (null, \"Baghdad\"), (null, \"Baku\"), (null, \"Bamako\"), (null, \"Bandar Seri Begawan\"), (null, \"Bangkok\"), (null, \"Bangui\"), (null, \"Banjul\"), (null, \"Basseterre\"), (null, \"Beijing\"), (null, \"Beirut\"), (null, \"Belgrade\"), (null, \"Belmopan\"), (null, \"Belize City\"), (null, \"Berlin\"), (null, \"Bern\"), (null, \"Bishkek\"), (null, \"Bissau\"), (null, \"Bloemfontein\"), (null, \"Cape Town\"), (null, \"Pretoria\"), (null, \"Bogotá\"), (null, \"Brades Estate\"), (null, \"Plymouth\"), (null, \"Brasília\"), (null, \"Bratislava\"), (null, \"Brazzaville\"), (null, \"Bridgetown\"), (null, \"Brussels\"), (null, \"Bucharest\"), (null, \"Budapest\"), (null, \"Buenos Aires\"), (null, \"Cairo\"), (null, \"Canberra\"), (null, \"Caracas\"), (null, \"Castries\"), (null, \"Cetinje\"), (null, \"Podgorica\"), (null, \"Charlotte Amalie\"), (null, \"Chișinău\"), (null, \"Cockburn Town\"), (null, \"Colombo\"), (null, \"Sri Jayawardenepura Kotte \"), (null, \"Conakry\"), (null, \"Copenhagen\"), (null, \"Cotonou\"), (null, \"Porto-Novo\"), (null, \"Dakar\"), (null, \"Damascus\"), (null, \"Dar es Salaam\"), (null, \"Dodoma\"), (null, \"Dhaka\"), (null, \"Dili\"), (null, \"Djibouti\"), (null, \"Doha\"), (null, \"Douglas\"), (null, \"Dublin\"), (null, \"Dushanbe\"), (null, \"El Aaiún\"), (null, \"Tifariti\"), (null, \"Flying Fish Cove\"), (null, \"Freetown\"), (null, \"Funafuti\"), (null, \"Gaborone\"), (null, \"George Town\"), (null, \"Georgetown\"), (null, \"Gibraltar\"), (null, \"Gitega (official)\"), (null, \"Bujumbura\"), (null, \"Guatemala City\"), (null, \"Gustavia\"), (null, \"Hagåtña\"), (null, \"Hamilton\"), (null, \"Hanoi\"), (null, \"Harare\"), (null, \"Hargeisa\"), (null, \"Havana\"), (null, \"Helsinki\"), (null, \"Honiara\"), (null, \"Islamabad\"), (null, \"Jakarta\"), (null, \"Jamestown\"), (null, \"Jerusalem\"), (null, \"Ramallah\"), (null, \"Juba\"), (null, \"Kabul\"), (null, \"Kampala\"), (null, \"Kathmandu\"), (null, \"Khartoum\"), (null, \"Kigali\"), (null, \"King Edward Point\"), (null, \"Kingston\"), (null, \"Kingstown\"), (null, \"Kinshasa\"), (null, \"Kuala Lumpur\"), (null, \"Putrajaya\"), (null, \"Kuwait City\"), (null, \"Kyiv\"), (null, \"La Paz\"), (null, \"Sucre\"), (null, \"Libreville\"), (null, \"Lilongwe\"), (null, \"Lima\"), (null, \"Lisbon\"), (null, \"Ljubljana\"), (null, \"Lobamba\"), (null, \"Mbabane \"), (null, \"Lomé\"), (null, \"London\"), (null, \"Luanda\"), (null, \"Lusaka\"), (null, \"Luxembourg\"), (null, \"Madrid\"), (null, \"Majuro\"), (null, \"Malabo\"), (null, \"Malé\"), (null, \"Managua\"), (null, \"Manama\"), (null, \"Manila\"), (null, \"Maputo\"), (null, \"Marigot\"), (null, \"Maseru\"), (null, \"Mata-Utu\"), (null, \"Mexico City\"), (null, \"Minsk\"), (null, \"Mogadishu\"), (null, \"Monaco\"), (null, \"Monrovia\"), (null, \"Montevideo\"), (null, \"Moroni\"), (null, \"Moscow\"), (null, \"Muscat\"), (null, \"Nairobi\"), (null, \"Nassau\"), (null, \"Naypyidaw\"), (null, \"N'Djamena\"), (null, \"New Delhi\"), (null, \"Ngerulmud\"), (null, \"Niamey\"), (null, \"Nicosia\"), (null, \"Nouakchott\"), (null, \"Nouméa\"), (null, \"Nukuʻalofa\"), (null, \"Nur-Sultan\"), (null, \"Nuuk\"), (null, \"Oranjestad\"), (null, \"Oslo\"), (null, \"Ottawa\"), (null, \"Ouagadougou\"), (null, \"Pago Pago\"), (null, \"Palikir\"), (null, \"Panama City\"), (null, \"Papeete\"), (null, \"Paramaribo\"), (null, \"Paris\"), (null, \"Philipsburg\"), (null, \"Phnom Penh\"), (null, \"Port Louis\"), (null, \"Port Moresby\"), (null, \"Port Vila\"), (null, \"Port-au-Prince\"), (null, \"Port of Spain\"), (null, \"Prague\"), (null, \"Praia\"), (null, \"Pristina\"), (null, \"Pyongyang\"), (null, \"Quito\"), (null, \"Rabat\"), (null, \"Reykjavík\"), (null, \"Riga\"), (null, \"Riyadh\"), (null, \"Road Town\"), (null, \"Rome\"), (null, \"Roseau\"), (null, \"Saipan\"), (null, \"San José\"), (null, \"San Juan\"), (null, \"San Marino\"), (null, \"San Salvador\"), (null, \"Santiago\"), (null, \"Valparaíso\"), (null, \"Santo Domingo\"), (null, \"São Tomé\"), (null, \"Sarajevo\"), (null, \"Seoul\"), (null, \"Singapore\"), (null, \"Skopje\"), (null, \"Sofia\"), (null, \"St. George's\"), (null, \"St. Helier\"), (null, \"St. John's\"), (null, \"St. Peter Port\"), (null, \"St. Pierre\"), (null, \"Stanley\"), (null, \"Stepanakert\"), (null, \"Stockholm\"), (null, \"Sukhumi\"), (null, \"Suva\"), (null, \"Taipei\"), (null, \"Tallinn\"), (null, \"Tarawa\"), (null, \"Tashkent\"), (null, \"Tbilisi\"), (null, \"Tegucigalpa\"), (null, \"Tehran\"), (null, \"Thimphu\"), (null, \"Tirana\"), (null, \"Tokyo\"), (null, \"Tórshavn\"), (null, \"Tripoli\"), (null, \"Tskhinvali\"), (null, \"Tunis\"), (null, \"Ulaanbaatar\"), (null, \"Vaduz\"), (null, \"Valletta\"), (null, \"The Valley\"), (null, \"Vatican City\"), (null, \"Victoria\"), (null, \"Vienna\"), (null, \"Vientiane\"), (null, \"Vilnius\"), (null, \"Warsaw\"), (null, \"Washington, D.C.\"), (null, \"Wellington\"), (null, \"West Island\"), (null, \"Willemstad\"), (null, \"Windhoek\"), (null, \"Yaoundé\"), (null, \"Yaren\"), (null, \"Yerevan\"), (null, \"Zagreb\")";
        String goodQuery = badQuery.replace("'", "").replace("\"", "'");

        Connection con = JdbcUtils.getConnection();
        try (PreparedStatement stmt = con.prepareStatement(goodQuery)) {
            stmt.executeUpdate();
        }

        List<String> capitals = Countries.getCapitalsFromDB();
        assertEquals(capitals.size(), 253);
    }

}