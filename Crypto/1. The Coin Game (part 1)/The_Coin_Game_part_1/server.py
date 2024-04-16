#!/usr/bin/python

from random import random, randint
flag = 'RDGCTF{Th3_Nash_3qui1ibrium_s01v3s}'

def Test_Ferma(p):
    for _ in range(100):
        a = randint(2, p-2)
        if pow(a, p-1, p) != 1:
            return False
    return True

def Signature(a, b, p, x, y):
    return (pow(a, x, p) * pow(b, y, p)) % p

def main():
    while True:
        print('Send a prime number greater than 1024 bits: ')
        p = input()
        if not p.isdigit():
            print('You did not enter a number')
            continue
        p = int(p)
        if p <= (1 << 1023):
            print('This is a small number')
            continue
        if not Test_Ferma(p):
            print('This is a composite number')
            continue
        print('This number fits all conditions')
        break
    a, b = randint(2, p-2), randint(2, p-2)
    print('Numbers for the signature: ')
    print(f'a = {a}\nb = {b}')
    balance_casino = balance_player = 200
    while balance_casino > 0 and balance_player > 0:
        print(f'Your balance:\t{balance_player}')
        print(f'Casino balance:\t{balance_casino}')
        print('Select a coin and send its signature:')
        sig_p = int(input())
        var = '2' if random() < 8/13 else '5'
        print(f'We think you took a coin: {var}')
        print('Sew the coin you have made up:')
        x = input()
        if not x in ['2', '5']:
            print('You can only guess 2 or 5')
            continue
        print('Send the number to verify the signature:')
        y = input()
        if not y.isdigit():
            print('You did not enter a number')
            continue
        if Signature(a, b, p, int(x), int(y)) != sig_p:
            print('The signature did not match, this game is canceled')
            continue
        if var == x:
            balance_casino += int(x)
            balance_player -= int(x)
        else:
            balance_casino -= 3
            balance_player += 3
    if balance_casino <= 0:
        print(f'I do not have any money left here is your flag: {flag}')
    else:
        print('You do not have any money left. Come back when they are')
            
if __name__ == "__main__":
    main()
