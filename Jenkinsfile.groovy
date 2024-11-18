pipeline {
    agent any

    parameters {
        string(name: 'PROJECT_NAME', defaultValue: 'new-java-project', description: 'Name of the project to be created')
        choice(name: 'JAVA_VERSION', defaultValue: 'java17', choices: ['java17', 'java21', 'java23'], description: 'Select the Java version')
        choice(name: 'DATABASE', choices: ['oracle', 'cassandra', 'postgres'], description: 'Select the database')
    }

    environment {
        WORKSPACE_DIR = "workspace/${params.PROJECT_NAME}"
    }

    stages {
        stage('Setup Workspace') {
            steps {
                echo "Setting up workspace..."
                sh """
                    rm -rf ${WORKSPACE_DIR}
                    mkdir -p ${WORKSPACE_DIR}
                """
            }
        }

        stage('Copy Template') {
            steps {
                echo "Copying template for database: ${params.DATABASE}..."
                script {
                    def templateDir = ""
                    if (params.DATABASE == 'postgres') {
                        templateDir = "spring-postgres-template"
                    } else if (params.DATABASE == 'oracle') {
                        templateDir = "spring-oracle-template"
                    } else if (params.DATABASE == 'cassandra') {
                        templateDir = "spring-cassandra-template"
                    }

                    // Copiar o template para o workspace
                    sh """
                        cp -R ${templateDir}/* ${WORKSPACE_DIR}/
                    """
                }
            }
        }

        stage('Configure Java Version') {
            steps {
                echo "Configuring project with Java version: ${params.JAVA_VERSION}..."
                script {
                    // Configura o Java para o projeto (Gradle, se necessário)
                    if (params.JAVA_VERSION == 'java17') {
                        sh "echo 'Java 17 selected'"
                    } else if (params.JAVA_VERSION == 'java21') {
                        sh "echo 'Java 21 selected'"
                    } else if (params.JAVA_VERSION == 'java23') {
                        sh "echo 'Java 23 selected'"
                    }
                }
            }
        }

        stage('Build Project') {
            steps {
                echo "Building the project..."
                sh """
                    cd ${WORKSPACE_DIR}
                    gradle build
                """
            }
        }

        stage('Finalize') {
            steps {
                echo "Project created successfully in: ${WORKSPACE_DIR}"
            }
        }
    }
}