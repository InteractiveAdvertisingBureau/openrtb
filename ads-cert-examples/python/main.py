import base64
import hashlib
import sys

from ecdsa import SigningKey, VerifyingKey, util, BadSignatureError


def load_keys():
    if len(sys.argv) < 3:
        raise Exception("invalid argument length")
    private_key = SigningKey.from_pem(open(sys.argv[1]).read())  # type: SigningKey
    public_key = VerifyingKey.from_pem(open(sys.argv[2]).read())  # type: VerifyingKey
    return private_key, public_key


def sign(private_key, digest, msg):
    return private_key.sign(msg, hashfunc=digest, sigencode=util.sigencode_der)


def verify(public_key, digest, msg, signature):
    try:
        return public_key.verify(signature, msg, hashfunc=digest, sigdecode=util.sigdecode_der)
    except BadSignatureError:
        return False


def main():
    private_key, public_key = load_keys()
    msg = ":".join([
        "2824f5d3-a2dc-49ec-8358-dbe92bd01ec2",  # source.tid
        "a:180720yz:1807241326",  # ad.pcv
        "newsite.com",  # site.domain
        "",  # app.bundle
        "",  # user.consent
        "vd",  # placement
        "192.168.1.1",  # device.ip
        "",  # device.ipv6
        "",  # device.ifa
        "",  # device.ua
        "",  # ad.video player size?
    ])
    signature = sign(private_key, hashlib.sha256, msg)
    print("publisher signature: %s" % base64.standard_b64encode(signature))
    if verify(public_key, hashlib.sha256, msg, signature):
        print("yay it worked!")
    else:
        print("it didn't work :(")


if __name__ == "__main__":
    main()
