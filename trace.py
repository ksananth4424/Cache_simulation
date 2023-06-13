import random
import copy

file_path = "trace4.txt"

arr=[]

for i in range (32768):
    arr.append(hex(random.randint(0,2**32-1)))
    
arr = copy.copy(arr)*128
random.shuffle(arr)
with open(file_path, 'w') as file:
    for i in range (len(arr)):
        file.write(str(arr[i])+" ")
