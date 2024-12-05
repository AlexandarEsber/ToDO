# Exercise 2

Alexandar Esber - 3725081
Programmentwicklung II - Wintersemester 2024/25  

---

## Project Overview  
This project is the second exercise for **Programmentwicklung II**. The backend can be found under the `/api` directory and the frontend in the `/frontend` directory.

---

## Prerequisites  
The project requires **Docker** to build and run the application. Ensure you have Docker installed on your system before proceeding. If not, you can install it from [Docker's official website](https://www.docker.com/get-started).

---

## Instructions  

1. **Navigate to the Project Directory**:  
   Search for the project folder where the `docker-compose.yml` file is located. Right-click on the folder, and you should see the option to "Open in Terminal." From there, you can enter all the following commands.

2. **Build and Run the Docker Image**:  
   Use `docker-compose` to build the Docker image and run the application. The following command will handle both building and starting the container:

   ```bash
   docker compose up --build
   ```

3. **Access the Application**:  
   Once the container is running, you can access the backend services exposed via Docker.

   For detailed backend documentation or API usage, refer to the README within the `/api` directory !!!

4. **Shutdown the Application**
   After you are done using the application, you can stop it by entering the following command in the same directory:

   ```bash
  docker compose down
  ```



---

