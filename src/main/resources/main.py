import json
import os
import functools
import time
from dotenv import load_dotenv
from aih_rag.embeddings import AzureOpenAIEmbedding
from aih_automaton.ai_models.AzureOpenai import AzureOpenAIModel
from pypdf import PdfReader
from rich import print
from aih_rag.vector_stores.deeplake import DeepLakeVectorStore
from aih_rag.schema import TextNode
from aih_rag.vector_stores.types import VectorStoreQuery
from pymongo import MongoClient
import sys
import argparse
import warnings
warnings.filterwarnings("ignore", category=UserWarning, module='pydantic')
warnings.filterwarnings("ignore", category=UserWarning, module='deeplake')
load_dotenv()
# Load environment variables
azure_endpoint = os.getenv("End_point")
azure_api_key = os.getenv("API_KEY")
azure_engine = os.getenv("Engine")
azure_api_version = os.getenv("Api_version")
# Initialize the embedding model
embedding_model = AzureOpenAIEmbedding(
    model="text-embedding-3-small",
    api_key=os.getenv("API_KEY"),
    azure_endpoint=os.getenv("End_point"),
    api_version="2024-02-01",
    azure_deployment="text-embedding-3-small"
)
# Initialize the model
model = AzureOpenAIModel(
    azure_api_key=os.getenv("API_KEY"),
    azure_api_version=os.getenv("Api_version"),
    azure_endpoint=os.getenv("End_point")
)
# Function to load the PDF file in chunks
def load_pdf_in_chunks(file_name, chunk_size=1000, overlap_size=20):
    try:
        # pdf_path = find_pdf_recurisvely(file_name, os.getcwd())
        pdf_path = file_name
        if not pdf_path:
            print("PDF file not found.")
            return []
        loader = PdfReader(pdf_path)
        pages = [page.extract_text() for page in loader.pages if page.extract_text()]
        chunk = "".join(pages)
        chunks = []
        for i in range(0, len(chunk), chunk_size - overlap_size):
            chunks.append(chunk[i:i+chunk_size])
        return chunks
    except Exception as e:
        print(f"An error occurred: {e}")
        return []
# Initialize the vector store
vectorstore_ = DeepLakeVectorStore(dataset_path='deeplake_db', overwrite=False)
# Load the PDF in chunks
chunks = load_pdf_in_chunks(r"D:\demo_apis\demohrms_apis\src\main\resources\HR-Policies-Manuals.pdf")
# Create a list of TextNode objects
text_nodes = [
    TextNode(
        text=chunk,
        embedding=embedding_model.get_text_embedding(chunk),
    )
    for chunk in chunks
]
# Add the text nodes to the vector store
added_nodes = vectorstore_.add(
    nodes=text_nodes
)
# Function to fetch data from MongoDB
def retry(max_retries=3, delay=1, fallback=None):
    def decorator_retry(func):
        @functools.wraps(func)
        def wrapper_retry(*args, **kwargs):
            retries = 0
            while retries < max_retries:
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    retries += 1
                    if fallback:
                        return fallback
                    else:
                        print(f"Error occurred: {e}. Retrying in {delay} seconds...")
                    time.sleep(delay)
            print("Max retries reached. Exiting.")
        return wrapper_retry
    return decorator_retry
@retry(max_retries=3, delay=1, fallback=True)
def fetch_from_mongo(query: dict):
    # Connect to MongoDB
    client = MongoClient("mongodb://localhost:27017/")
    db = client["admin"]
    collection = db["employee_data"]
    query_result = []
    if query.get('EmployeeID', "") == "":
        print("No EmployeeID provided")
        return []
    # Fetching the data from MongoDB based on the query
    results = collection.find(query)
    for item in results:
        item["_id"] = str(item["_id"])
        item["HireDate"] = str(item["HireDate"])
        query_result.append(item)
    # with open("query_result.json", "w", encoding="utf-8") as f:
    #     json.dump(query_result, f, indent=4)
    return query_result
# Example query data (you can modify the query to suit your data)
data = {
    "EmployeeID": "95D7-1CE9"
}
# Calling the function with the query
user_data = fetch_from_mongo(data)
# print(user_data)
def handle_query(user_query, embedding_text, user_data):
    system_persona = f"""
    Answer the Query based on User Data: {user_data} and Context: {embedding_text}
    
    
    
        **Features:**
        1. **HR related questions:**
           - All the HR related Policies
           - Joining formalities
           - Features of HR System
           - List of Holidays
           - Leave balances
           - Exit Policies
           - Loan policies
           - Mediclaim policies
           - Travel policies
           - Company Policies
           - Reimbursement process
           - Download Offer letter
           - Appraisal letter
           - Process to Enter investment in the HR system for Taxation
           - Employee performance data
           - Emergency contact person details of the employees (restricted)
           - Birthday/anniversary
           - L&D data
           - Skill matrix or skill repository
           - PF UAN Number
           
           
           
        2. **IT related questions:**
           - Laptop policies
           - Whom to contact for any IT related issues
           
           
           
        3. **Finance related questions:**
           - Payslip
           - Salary breakup
           - Form 16
           
           
           
        **Instructions**
        - You are an HRMS Chatbot expert in query resolution.
        - You are holding the persona of an HR professional.
        - You will not answer out of this persona.
        - You are capable of assisting with a wide range of HR-related tasks, including retrieving and updating employee information, handling leave requests, salary(In Rupees), providing payroll information, and answering questions about company policies and procedures.
        - if user ask about the salary slip then give the URL given in the user data:{user_data} with proper markdown.
    """
    messages = [
        {
            "role": "system",
            "content": f"You will not answer out of this {system_persona} "
        },
        {
            "role": "user",
            "content": " Answer the Query: " + user_query
        }
    ]
    # print(prompt)
    response = model.generate_text(messages=messages)
    return response
# Example user query
# user_query = "what is my salary and also tell me what i get if my child excel in studies."
# # Perform the query and print the results
# query_results = vectorstore_.query(
#     query=VectorStoreQuery(
#         query_embedding=embedding_model.get_query_embedding(user_query),
#         similarity_top_k=5
#     )
# )
# # Extract text from query results
# embedding_text = [node.text for node in query_results.nodes]
# # Handle the query and get the response
# response = handle_query(user_query, embedding_text, user_data)
# print(response)
# with open("response.json", "w", encoding="utf-8") as f:
#     json.dump({"response": response}, f, indent=4)
if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="HRMS Chatbot for querying HR information.")
    parser.add_argument('--employee_id', type=str, required=True, help='Employee ID to query information for')
    parser.add_argument('--query', type=str, required=True, help='Query about HR policies or employee information')
    args = parser.parse_args()
    # Replace the default data with the command-line arguments
    data = {
        "EmployeeID": args.employee_id
    }
    user_query = args.query
    # Fetch user data from MongoDB
    user_data = fetch_from_mongo(data)
    # Perform the query and print the results
    query_results = vectorstore_.query(
        query=VectorStoreQuery(
            query_embedding=embedding_model.get_query_embedding(user_query),
            similarity_top_k=5
        )
    )
    # Extract text from query results
    embedding_text = [node.text for node in query_results.nodes]
    # Handle the query and get the response
    response = handle_query(user_query, embedding_text, user_data)
    # print(response)
    # Save the response to a JSON file
    with open("response.json", "w", encoding="utf-8") as f:
        json.dump({"response": response}, f, indent=4)