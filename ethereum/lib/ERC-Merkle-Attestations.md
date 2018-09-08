## ERC - Merkle Tree Attestations

### Introduction

It's often needed that an Ethereum smart contract must verify a claim (I live in Australia) attested by a valid attester.

For example, an ICO smart contract needs to verify a claim by a participant, Alice, that she lives in Australia. Alice's claim might be signed by a local Justice of the Peace. We call it "Alice's residency is attested by an attestation issuer".

Unlike previous attempts, we assume that the attestation is signed and issued off the blockchain in a Merkle Tree format. Only a part of the Merkle tree is revealed by Alice at each use. Therefore we avoid the privacy problem often associated with issuing attestations on chain. We also assume Alice has multiple signed Merkle Trees for the same factual claim to avoid her transactions being linkable.

## Purpose
This ERC provides an interface and reference implementation for smart contracts that need users to provide an attestation and validate it.

### Draft implementation
```
contract MerkleTreeAttestationInterface {
    struct Attestation
    {
        bytes32[] merklePath;
        bool valid;
        uint8 v;
        bytes32 r;
        bytes32 s;
        address attester;
        address recipient;
        bytes32 salt;
        bytes32 key;
        bytes32 val;
    }

    function validate(Attestation attestation) public returns(bool);
}

```
### Relevant implementation examples
[Here](https://github.com/alpha-wallet/blockchain-attestation/blob/master/ethereum/lib/MerkleTreeAttestation.sol) is an example implementation of the MerkleTreeAttestationInterface
[Here](https://github.com/alpha-wallet/blockchain-attestation/blob/master/ethereum/example-james-squire/james-squire.sol) is an example service which would use such a merkle tree attestation

### Related ERC's
#1388 #1386
