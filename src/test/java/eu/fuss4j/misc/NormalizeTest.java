package eu.fuss4j.misc;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 14, 2017
 */
public class NormalizeTest {

    @Test
    public void normNormalizesInput() throws Exception {
        String strFr = "Générateur de texte aléatoire";
        String expFr = "Generateur de texte aleatoire";

        assertEquals("The input text should be normalized by Normalize::norm", expFr, Normalize.norm(strFr));
    }

    @Test
    public void applyNormalizesInput() throws Exception {
        String strRo = "Şi-am zis verde de albastru,\nmă doare un cal măiastru";
        String expRo = "Si-am zis verde de albastru,\nma doare un cal maiastru";

        assertEquals("The input text should be normalized by Normalize::apply", expRo, new Normalize().apply(strRo));
    }
}
