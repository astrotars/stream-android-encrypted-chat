import dotenv from 'dotenv';

const { JwtGenerator } = require('virgil-sdk');
const { initCrypto, VirgilCrypto, VirgilAccessTokenSigner } = require('virgil-crypto');

dotenv.config();

async function getJwtGenerator() {
  await initCrypto();

  const virgilCrypto = new VirgilCrypto();
  // initialize JWT generator with your App ID and App Key ID you got in
  // Virgil Dashboard.
  return new JwtGenerator({
    appId: process.env.VIRGIL_APP_ID,
    apiKeyId: process.env.VIRGIL_KEY_ID,
    apiKey: virgilCrypto.importPrivateKey(process.env.VIRGIL_PRIVATE_KEY),
    accessTokenSigner: new VirgilAccessTokenSigner(virgilCrypto)
  });
}

const generatorPromise = getJwtGenerator();

exports.virgilCredentials = async (req, res) => {
  const generator = await generatorPromise;
  const virgilJwtToken = generator.generateToken(req.user.sender);

  res.json({ token: virgilJwtToken.toString() });
};
