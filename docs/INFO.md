# Task

## Dev

### Run JHipster

The process is described [here.](https://www.jhipster.tech/installation/#docker-installation-for-advanced-users-only)

Start JHipster

    docker-compose -f docs/docker-compose-jhipster.yaml up

Connect to the container to install KHipster as root:

    docker exec -it --user root jhipster bash
    npm install -g generator-jhipster-kotlin
    exit

Connect to use KHipster as normal user to avoid locking local files:

    docker exec -it jhipster bash
    khipster --skip-fake-data

Congratulations, JHipster execution is complete!

Apply JDL:

Follow [the process:](https://www.jhipster.tech/creating-an-entity/#jhipster-uml-and-jdl-studio)

    docker exec -it jhipster bash
    khipster jdl docs/jhipster-jdl-inplace.jdl


Stop JHipster

    docker-compose -f docs/docker-compose-jhipster.yaml down --volumes

### Run

Launch the infra:

    cd src/main/docker
    docker-compose -f postgresql.yml up

Launch App with initial data (make sure you use Java 11):

    ./mvnw --version
    ./mvnw

Stop infra:

    docker-compose -f postgresql.yml down --volumes






