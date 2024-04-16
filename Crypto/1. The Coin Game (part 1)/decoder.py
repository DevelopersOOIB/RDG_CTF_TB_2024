import telnetlib
from random import randint

HOST = "10.0.2.15"
PORT = 3680

tn = telnetlib.Telnet(HOST, PORT)

def Test_Ferma(p):
    for _ in range(100):
        a = randint(2, p-2)
        if pow(a, p-1, p) != 1:
            return False
    return True
    
def getPrime(L):
    while True:
        p = randint(1 << L - 1, 1 << L)
        if Test_Ferma(p):
            return p
			
def Signature(a, b, p, x, y):
    return (pow(a, x, p) * pow(b, y, p)) % p

def main():
    tn.read_until(b"\n")
    p = getPrime(1024)
    tn.write(f'{p}'.encode() + b'\n')
    tn.read_until(b"\n")
    tn.read_until(b"\n")
    a = tn.read_until(b"\n")
    a = int(a[4:])
    b = tn.read_until(b"\n")
    b = int(b[4:])
    g = s = 200
    while g > 0 and s > 0:
        g = tn.read_until(b"\n").decode()
        g = int(g[g.index('\t') + 1:])
        s = tn.read_until(b"\n").decode()
        s = int(s[s.index('\t') + 1:])
        tn.read_until(b"\n")
        x = 5
        y = randint(2, p-2)
        sig = Signature(a, b, p, x, y)
        tn.write(f'{sig}'.encode() + b'\n')
        var = tn.read_until(b"\n").decode()
        var = int(var[var.index(':') + 1:])
        tn.read_until(b"\n")
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
