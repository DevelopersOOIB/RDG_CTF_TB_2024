from Cryptodome.Util.number import getPrime, bytes_to_long

flag = b'RDGCTF{Wiener_attack_how_easy_it_is_to_make_a_mistake}'

def getKey(L):
    p = getPrime(L // 2 + 1)
    q = getPrime(L // 2 + 1)
    n = p*q
    fi = (p-1)*(q-1)
    e = getPrime(L // 8)
    d = pow(e, -1, fi)
    return d, e, n

def main():
    e, d, n = getKey(4096)
    m = bytes_to_long(flag)
    c = pow(m, e, n)
    with open('output.txt', 'w') as file:
        file.write(f'c = {c}\nn = {n}\ne = {e}\n')

if __name__ == "__main__":
    main()
