#include <stdio.h>
#include<fcntl.h>

int main(int argc, char** argv)
{
    int fd = open("/sys/class/gpio/gpio88/value", O_RDONLY);
    unsigned char buf[10];
    int len = read(fd, buf, 10);
    printf("%d %x\n", len, buf[0]);
    len = read(fd, buf, 10);
    printf("%d %x\n", len, buf[0]);
    close(fd);
}