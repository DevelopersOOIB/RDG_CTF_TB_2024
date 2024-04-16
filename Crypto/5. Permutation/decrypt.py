from random import sample

def main():
    L = 109
    with open('cipherV.bmp', 'rb') as file:
        text = file.read()
    n = int.from_bytes(text[10:14], byteorder = "little")

    text_pixel = [text[i: i+3] for i in range(n, len(text), 3)]
    len_pixel = len(text_pixel)

    text_table = [[text_pixel[i + j] for j in range(0, len_pixel, L)] for i in range(L)]
    text_table_stat = [sorted([text_table[i][j] + text_table[i+1][j] for j in range(len(text_table[0]))]) for i in range(L - 1)]

    print(len(text_table[0]))
    with open('cipherG.bmp', 'rb') as file:
        text = file.read()
    n = int.from_bytes(text[10:14], byteorder = "little")

    text_pixel = [text[i: i+3] for i in range(n, len(text), 3)]
    len_pixel = len(text_pixel)
    text_table = [[text_pixel[i + j] for j in range(0, len_pixel, L)] for i in range(L)]

    key = [-1] * 109
    for i in range(L):
        print(i)
        for j in range(L):
            if i == j:
                continue
            stat = sorted([text_table[i][k] + text_table[j][k] for k in range(len(text_table[0]))])
            if stat in text_table_stat:
                key[text_table_stat.index(stat)] = i
                key[text_table_stat.index(stat) + 1] = j
                break
            
    cipher = text[:n]
    cipher_table = [text_table[k] for k in key]
    c = b''.join([b''.join([cipher_table[i][j] for i in range(L)]) for j in range(len(text_table[0]))])
    cipher += c
    with open('flag.bmp', 'wb') as file:
        file.write(cipher)
            

if __name__ == "__main__":
    main()
