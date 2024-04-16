from Cryptodome.Util.number import getPrime, bytes_to_long
from random import randint

flag = b'RDGCTF{LLL_alg0r1thm_1s_f0r3v3r_1n_my_h3art}'

def main():
    p = getPrime(2048)
    x = bytes_to_long(flag[:len(flag) // 2])
    y = bytes_to_long(flag[len(flag) // 2:])
    for i in range(2):
        a, b = randint(1, p-1), randint(1, p-1)
        z = (a*x + b*y) % p
        with open(f'key{i}.txt', 'w') as file:
            file.write(f'a = {a}\nb = {b}\nz = {z}\np = {p}')
    
if __name__ == "__main__":
    main()
