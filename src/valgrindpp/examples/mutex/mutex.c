#include <stdio.h>
#include <pthread.h>

void* produce(void* arg);
void* consume(void* arg);

int value = 0;
pthread_t producer, consumer;
pthread_mutex_t write, read;

int main() {
    int error;

    if(error = pthread_mutex_init(&write, NULL)) return error;
    if(error = pthread_mutex_init(&read, NULL)) return error;
    pthread_mutex_lock(&read);

    if(error = pthread_create(&producer, NULL, &produce, NULL)) return error;
    if(error = pthread_create(&consumer, NULL, &consume, NULL)) return error;

    pthread_join(producer, NULL);
    pthread_join(consumer, NULL);

    return 0;
}

void* produce(void* arg) {
    for(int i = 0; i < 10; i++) {
        pthread_mutex_lock(&write);
        value = i;
        pthread_mutex_unlock(&read);
    }

    pthread_mutex_lock(&write);
    value = -1;
    pthread_mutex_unlock(&read);
}

void* consume(void* arg) {
    while(1) {
        pthread_mutex_lock(&read);

        if(value < 0) break;
        printf("%d\n", value);
        
        pthread_mutex_unlock(&write);
    }
}