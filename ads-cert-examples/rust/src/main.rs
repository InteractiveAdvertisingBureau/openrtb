extern crate openssl;
extern crate base64;

use std::env;
use std::path::Path;

use openssl::error::ErrorStack;
use openssl::hash::MessageDigest;
use openssl::pkey::PKey;
use openssl::pkey::Private;
use openssl::pkey::Public;
use openssl::sign::Signer;
use openssl::sign::Verifier;

fn main() -> Result<(), Error> {
    // load public and private key
    let (private, public) = load_keys().unwrap();
    // create our message
    let msg = [
        "2824f5d3-a2dc-49ec-8358-dbe92bd01ec2", // source.tid
        "a:180720yz:1807241326",                // ad.pcv
        "newsite.com",                          // site.domain
        "",                                     // app.bundle
        "",                                     // user.consent
        "vd",                                   // placement
        "192.168.1.1",                          // device.ip
        "",                                     // device.ipv6
        "",                                     // device.ifa
        "",                                     // device.ua
        "",                                     // ad.video player size?
    ].join(":");
    // sign it
    let signature = sign(&private, MessageDigest::sha256(), &msg)
        .map_err(|e| Error::OpensslError(e))?;
    println!("publisher signature: {}", base64::encode(&signature));
    // verify it
    let result = verify(
        &public,
        MessageDigest::sha256(),
        &msg,
        &signature,
    ).map_err(|e| Error::OpensslError(e))?;
    match result {
        true => println!("yay it worked!"),
        false => println!("it didn't work :("),
    }
    Ok(())
}

#[derive(Debug)]
enum Error {
    InvalidLength,
    IO(std::io::Error),
    OpensslError(ErrorStack),
}

fn load_keys() -> Result<(PKey<Private>, PKey<Public>), Error> {
    let args: Vec<String> = env::args().collect();
    if args.len() < 3 {
        return Err(Error::InvalidLength);
    }
    let private_key_path = Path::new(&args[1]);
    let public_key_path = Path::new(&args[2]);
    let private = read_file(private_key_path)?;
    let public = read_file(public_key_path)?;
    let private = PKey::private_key_from_pem(&private).map_err(|e| Error::OpensslError(e))?;
    let public = PKey::public_key_from_pem(&public).map_err(|e| Error::OpensslError(e))?;
    Ok((private, public))
}

fn sign(
    private_key: &PKey<Private>,
    digest: MessageDigest,
    data: &str,
) -> Result<Vec<u8>, ErrorStack> {
    let mut signer = Signer::new(digest, &private_key)?;
    signer.update(data.as_bytes())?;
    signer.sign_to_vec()
}

fn verify(
    public_key: &PKey<Public>,
    digest: MessageDigest,
    msg: &str,
    signature: &[u8],
) -> Result<bool, ErrorStack> {
    let mut verifier = Verifier::new(digest, public_key)?;
    verifier.update(msg.as_bytes())?;
    verifier.verify(&signature)
}

fn read_file(path: &std::path::Path) -> Result<Vec<u8>, Error> {
    use std::fs::File;
    use std::io::Read;
    let mut file = File::open(path).map_err(|e| Error::IO(e))?;
    let mut contents: Vec<u8> = Vec::new();
    file.read_to_end(&mut contents).map_err(|e| Error::IO(e))?;
    Ok(contents)
}
