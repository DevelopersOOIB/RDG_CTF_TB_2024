import telnetlib
from random import randint

HOST = "10.0.2.15"
PORT = 3680

tn = telnetlib.Telnet(HOST, PORT)

def KTO(A, M):
    Mod = 1
    for m in M:
        Mod *= m
    x = 0
    for i in range(len(A)):
        m = Mod // M[i]
        x += A[i] * m * pow(m, -1, M[i])
    return x % Mod

def log(a, g, q, p):
    b = pow(g, (p - 1) // q, p)
    for k in range(q):
        if pow(b, k, p) == a:
            return k
    return None

def log_discret(y, g, Q, p):
    K = [log(pow(y, (p - 1) // i, p), g, i, p) for i in Q]
    return KTO(K, Q)    
			
def Signature(a, b, p, x, y):
    return (pow(a, x, p) * pow(b, y, p)) % p

def main():
    p = 0x1d65290b673d90c1c4ff309b7ebed2d3f277c9d1799f33219bdc36cbff6e8ce6afc9286e442befdb2e72d0fd2db4b02b785a997f9e70154be933946da2bf0dfd3189a2ccdfc05f5d5709b78701b5ebd23039041d7ed40653b83792f3c5ce592a80b3318ffad786e74e69aea06e27d4f9ce96a9ce0973b5160c961e62d7c8681a87
    Q = [i for i in range(2, 2**16) if (p - 1) % i == 0]
    tn.read_until(b"\n")
    tn.write(f'{p}'.encode() + b'\n')
    tn.read_until(b"\n")
    tn.read_until(b"\n")
    a = tn.read_until(b"\n")
    a = int(a[4:])
    b = tn.read_until(b"\n")
    b = int(b[4:])
    y2 = 1
    sig = Signature(a, b, p, 2, y2)
    y5 = log_discret((sig * pow(a, -5, p)) % p, b, Q, p)
    g = s = 2000
    while g > 0 and s > 0:
        g = tn.read_until(b"\n").decode()
        g = int(g[g.index('\t') + 1:])
        s = tn.read_until(b"\n").decode()
        s = int(s[s.index('\t') + 1:])
        tn.read_until(b"\n")
        tn.write(f'{sig}'.encode() + b'\n')
        var = tn.read_until(b"\n").decode()
        var = int(var[var.index(':') + 1:])
        tn.read_until(b"\n")
        if var == 2:
            x, y = 5, y5
        else:
            x, y = 2, y2
        tn.write(f'{x}'.encode() + b'\n')
        tn.read_until(b"\n")
        tn.write(f'{y}'.encode() + b'\n')
        if var == x:
            s += x
            g -= x
        else:
            s -= 3
            g += 3
    print(tn.read_until(b"\n"))

if __name__ == "__main__":
    main()
