FROM jenkins/jenkins:lts

USER root

# Atualizar repositórios e instalar pacotes necessários
RUN apt-get update && apt-get install -y --no-install-recommends \
    wget \
    unzip \
    curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

RUN jenkins-plugin-cli --plugins pipeline-model-definition pipeline-github

USER jenkins