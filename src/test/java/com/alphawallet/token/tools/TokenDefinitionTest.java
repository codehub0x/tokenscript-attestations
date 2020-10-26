package com.alphawallet.token.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.alphawallet.token.entity.ContractInfo;
import com.alphawallet.token.entity.ParseResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;


public class TokenDefinitionTest implements ParseResult
{
    private File entryTokenTestFile = new File("src/test/ts/entrytoken.canonicalized.xml");

    @Test
    public void TokenInformationCanBeExtracted() throws IOException, SAXException {
        assertTrue(entryTokenTestFile.exists());
        TokenDefinition entryToken = new TokenDefinition(new FileInputStream(entryTokenTestFile), new Locale("en"), this);
        assertFalse(entryToken.attributes.isEmpty());
        for (String contractName : entryToken.contracts.keySet())
        {
            assertNotEquals(0, contractName.length());
        }

        // test contract address extraction
        String holdingContract = entryToken.holdingToken;
        ContractInfo ci = entryToken.contracts.get(holdingContract);

        assertTrue(entryToken.contracts.size() > 0); //we have at least one address

        for (int networkId : ci.addresses.keySet())
        {
            for (String address : ci.addresses.get(networkId))
            {
                assertEquals(40 + 2, address.length());
            }
        }
    }

    @Test
    public void BadLocaleShouldThrowException() throws IOException, SAXException {
        Assertions.assertThrows(SAXException.class, () -> {
            TokenDefinition ticketAsset = new TokenDefinition(new FileInputStream(entryTokenTestFile), new Locale("asdf"), this);
            assertNotNull(ticketAsset);
        });
    }

    @Override
    public void parseMessage(ParseResultId parseResult)
    {
        switch (parseResult)
        {
            case OK:
                System.out.println("Schema date is correct.");
                break;
            case XML_OUT_OF_DATE:
                System.out.println("Parsing outdated schema. It's an older schema but it checks out.");
                break;
            case PARSER_OUT_OF_DATE:
                System.out.println("Parser attempting to parse future schema. Code base needs to be updated.");
                fail();
                break;
            case PARSE_FAILED:
                System.out.println("Parser Error.");
                fail();
                break;
        }
    }
}
