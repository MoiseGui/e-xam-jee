# e-xam: online exam management system

## Fet Started

run the commands below :

```
docker build -t e-xam:1.0 .
docker run -it -p 8080:8080 e-xam:1.0
```

The application is available at http://localhost:8080/admin-starter

if container is still running, you can stop it with the command below:

```
docker container ls
docker container kill <$id>
```