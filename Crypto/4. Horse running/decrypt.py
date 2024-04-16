
def num_var(x, y, A):
    res = 0
    for k in range(2):
        for i in range(2):
            for j in range(2):
                a = x + (2 - k) * (-1)**i
                b = y + (1 + k) * (-1)**j
                if 0 <= a <= 7 and 0 <= b <= 7 and A[a][b] == 0:
                    res += 1
    return res

def next_xy(x, y, A):
    x1, y1, n = 8, 8, 9
    for k in range(2):
        for i in range(2):
            for j in range(2):
                a = x + (2 - k) * (-1)**i
                b = y + (1 + k) * (-1)**j
                if 0 <= a <= 7 and 0 <= b <= 7 and A[a][b] == 0:
                    r = num_var(a, b, A)
                    if r < n:
                        x1, y1, n = a, b, r 
    return x1, y1, n

def main():
    with open('cipher.txt', 'r') as file:
        cipher = file.readlines()
    flag = 'R'
    A = [[0] * 8 for _ in range(8)]
    i, j = 0, 0
    A[i][j] = 1
    for k in range(2, 65):
        i, j, n = next_xy(i, j, A)
        if n == 9:
            break
        else:
            A[i][j] = k
            flag += cipher[i][j]
    print(flag)

if __name__ == "__main__":
    main()
