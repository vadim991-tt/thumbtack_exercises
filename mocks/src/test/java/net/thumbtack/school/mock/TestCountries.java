package net.thumbtack.school.mock;

import net.thumbtack.school.mock.capitals.Countries;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Countries.class)
public class TestCountries {

    @Test
    public void testUrl() throws Exception {
        HttpURLConnection http = mock(HttpURLConnection.class);
        URL url = mock(URL.class);
        when(url.openConnection()).thenReturn(http);
        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(url);

        try {
            Countries.downloadPage("Moscow");
        } catch (NullPointerException e) {
            //ignore
        }

        PowerMockito.verifyNew(URL.class).withArguments("http://restcountries.eu/rest/v2/capital/Moscow");
    }

    @Test
    public void testDownloadPage() throws Exception {
        InputStream stream = new ByteArrayInputStream(
                ("[{\"name\":\"Russian Federation\",\"topLevelDomain\":[\".ru\"],\"alpha2Code\":\"RU\",\"alpha3Code\"" +
                        ":\"RUS\",\"callingCodes\":[\"7\"],\"capital\":\"Moscow\",\"altSpellings\":[\"RU\",\"Rossiya\"," +
                        "\"Russian Federation\",\"Российская Федерация\",\"Rossiyskaya Federatsiya\"],\"region\":\"Europe\"" +
                        ",\"subregion\":\"Eastern Europe\",\"population\":146599183,\"latlng\":[60.0,100.0],\"demonym\":\"" +
                        "Russian\",\"area\":1.7124442E7,\"gini\":40.1,\"timezones\":[\"UTC+03:00\",\"UTC+04:00\",\"UTC+06:00\"," +
                        "\"UTC+07:00\",\"UTC+08:00\",\"UTC+09:00\",\"UTC+10:00\",\"UTC+11:00\",\"UTC+12:00\"],\"borders\":[\"AZE\"," +
                        "\"BLR\",\"CHN\",\"EST\",\"FIN\",\"GEO\",\"KAZ\",\"PRK\",\"LVA\",\"LTU\",\"MNG\",\"NOR\",\"POL\",\"UKR\"],\"nativeName\":" +
                        "\"Россия\",\"numericCode\":\"643\",\"currencies\":[{\"code\":\"RUB\",\"name\":\"Russian ruble\",\"symbol\":\"Rub\"}],\"languages\"" +
                        ":[{\"iso639_1\":\"ru\",\"iso639_2\":\"rus\",\"name\":\"Russian\",\"nativeName\":\"Русский\"}],\"translations\":{\"de\":\"Russland\"" +
                        ",\"es\":\"Rusia\",\"fr\":\"Russie\",\"ja\":\"ロシア連邦\",\"it\":\"Russia\",\"br\":\"Rússia\",\"pt\":\"Rússia\",\"nl\":\"Rusland\",\"hr" +
                        "\":\"Rusija\",\"fa\":\"روسیه\"},\"flag\":\"https://restcountries.eu/data/rus.svg\",\"regionalBlocs\":[{\"acronym\":\"EEU\",\"name\":\"" +
                        "Eurasian Economic Union\",\"otherAcronyms\":[\"EAEU\"],\"otherNames\":[]}],\"cioc\":\"RUS\"}]")
                        .getBytes()
        );

        HttpURLConnection http = mock(HttpURLConnection.class);
        when(http.getContent()).thenReturn(stream);
        URL url = mock(URL.class);
        when(url.openConnection()).thenReturn(http);
        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(url);

        String result = Countries.downloadPage("Moscow");
        assertEquals("Moscow\n" +
                "{\"name\":\"Russian Federation\"\n" +
                "\"currencies\":[{\"code\":\"RUB\",\"name\":\"Russian ruble\",\"symbol\":\"Rub\"}]\n", result);
    }


//    @Test
//    public void testDownloadInformationAboutCountries() throws IOException, SQLException {
//        List<String> list = Countries.downloadInformationAboutCountries();
//        List<String> capitals = Countries.downloadInformationAboutCountries();
//        assertEquals(list.size(), capitals.size());
//    }
}