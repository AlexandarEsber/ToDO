# Exercise 2  
## Alexander Esber - 3725081  
### Programmentwicklung II - Wintersemester 2024/25  

---

## Project Overview  
This project is the second exercise for **Programmentwicklung II**. The backend can be found under the `/api` directory.

---

## Prerequisites  
The project requires **Docker** to build and run the application. Ensure you have Docker installed on your system before proceeding. If not, you can install it from [Docker's official website](https://www.docker.com/get-started).

---

## Instructions  

1. **Navigate to the Project Directory**:  
   Open your terminal and move to the root directory of the project where the `docker-compose.yml` file is located.

   ```bash
   cd path/to/project
   ```

2. **Build and Run the Docker Image**:  
   Use `docker-compose` to build the Docker image and run the application. The following command will handle both building and starting the container:

   ```bash
   docker-compose up --build
   ```

3. **Access the Application**:  
   Once the container is running, you can access the backend services exposed via Docker.

---

## Notes  

- Stop the application by running the following command in the same directory:
  
  ```bash
  docker-compose down
  ```

- For detailed backend documentation or API usage, refer to the README within the `/api` directory.

---

