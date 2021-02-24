<?xml version="1.0" encoding="UTF-8"?>
<asnx:module xmlns:asnx="urn:ietf:params:xml:ns:asnx" name="AttestationRequest">
  <import name="SubjectPublicKeyInfo" schemaLocation="AttestationFramework.asd"/>
  <import name="Proof" schemaLocation="ProofOfExponent.asd"/>

  <namedType name="Identity">
    <type>
      <sequence>
        <element name="payload" type="IdentityPayload">
          <annotation>The user's identity proof payload.</annotation>
        </element>
        <element name="subjectPublicKeyInfo" type="SubjectPublicKeyInfo">
          <annotation>The information about the user's Ethereum key that is supposed to be attested and used to sign this request</annotation>
        </element>
      </sequence>
    </type>
  </namedType>

  <namedType name="IdentityPayload">
    <type>
      <sequence>
        <element name="type" type="IdentifierType">
          <annotation>Integer describing the type of identifier.</annotation>
        </element>
        <element name="proof" type="Proof">
          <annotation>A proof containing the user's chosen hiding for his certificate and a proof that this has been correctly constructed.</annotation>
        </element>
      </sequence>
    </type>
  </namedType>

  <namedType name="IdentifierType">
    <type>
      <namedNumberList>
        <namedNumber name="phone" number="0"/>
        <namedNumber name="email" number="1"/>
      </namedNumberList>
    </type>
  </namedType>
</asnx:module>
