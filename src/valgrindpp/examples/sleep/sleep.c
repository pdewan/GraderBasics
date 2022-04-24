#include <stdio.h>
#include <pthread.h>
#include <unistd.h>

void* produce(void* arg);
void* consume(void* arg);

int value = 0;

int main() {
    pthread_t producer, consumer;

    int error;

    if(error = pthread_create(&producer, NULL, &produce, NULL)) return error;
    if(error = pthread_create(&consumer, NULL, &consume, NULL)) return error;

    pthread_join(producer, NULL);
    pthread_join(consumer, NULL);

    return 0;
}

void* produce(void* arg) {
    for(int i = 0; i < 10; i++) {
        value = i;
        sleep(1);
    }
    value = -1;
}

void* consume(void* arg) {
    while(1) {
        if(value < 0) break;
        printf("%d\n", value);
        sleep(1);
    }
}