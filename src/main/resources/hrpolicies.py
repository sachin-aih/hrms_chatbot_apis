import json
import os

# Define the policies content
text = {
    "response": """'XYZ Corporate HR Policies \n \n \n \n \n \n \nThese consultants have
to submit the invoice at end of the month and they are not entitled for  
the \nProvident Fund, ESIC and other statutory benefits. \nAll other     
benefits of the company will depend upon the grades and hierarchy of the 
consultant. \nThe same will be approved by the Managing Director at the  
time of joining of the consultant. \n \nSection : 20 Review and Amendment
\nManagement shall review this policy periodically and  amendments       
required, if any shall be made \naccordingly. \nSection : 21 Residual    
Power \n \nThis policy is basically guidelines and the management        
reserves the right to withdraw / modify to \nsuit organization’s
philosophy at any time without assigning any reason whatsoever.
\nEFFECTIVE \n \nCommencement Of Policy August 21, 2018 \n \n \n \n \n   
\nApproved By :  SD/-  \nMr . ABC Kumar '"""
}

# Get the root path of the project
base_path = os.path.abspath(os.getcwd())  # This gives you the root path of the project

# Define the output path for the JSON file
output_path = os.path.join(base_path, "src", "main", "resources", "hr_policies.json")

# Write the content to the file
with open(output_path, "w") as json_file:
    json.dump(text, json_file)

print(f"JSON file generated at {output_path}")
