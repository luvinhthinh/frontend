WORK DONE

- DB Creation of 4 tables : Customer, Product, Sales_Order and Order_Line
- Domain(Model) Class Creation: Customer, Product, SalesOrder and OrderLine
- DAO interfaces and classes creation. Actions supported includes insert, delete, update, search and selectAll
- Service interfaces and implementation classes
- DAO test on Customer, Product and SalesOrder
- Controller Class creation
- Integration with Swing App : Customer and Product
- Maven : Build, Test, package war, and deploy on tomcat 


- Logic of Insertion and Update of Sales Order:
  Assumption: 
  	Current Quantity for Product 1 is 100
  	Current Credit for Customer A 1000
  	  
  + insertion :
     . New Quantity = Current Quantity - Input Quantity .
     If Input Quantity is 20 
     Then New Quantity = 100 - 20 = 80   
     
     
     . New Credit = Current Credit + Input Total Price
     If Input Total Price = 200
     Then New Credit = 1000 + 200
     
  + update : 
     . New Quantity = Current Quantity - (New Input Quantity - Old Input Quantity)
     
     If New Input Quantity is 20 and Old Input Quantity is 10
     Then before update : Quantity = 100 - 10 = 90
     And after update : Quantity = 90 - (20 - 10) = 80 
     
     If New Input Quantity is 5 and Old Input Quantity is 10
     Then before update : Quantity = 100 - 10 = 90
     And after update : Quantity = 90 - (5 - 10) = 95
     
     . New Credit = Current Credit + (New Input Total Price - Old Input Total Price)
     
     If New Input Total Price is 300 and Old Input Total Price is 200
     Then before update : Credit = 1000 + 200 = 1200
     And after update : Credit = 1200 + (300 - 200) = 1300
     
	 If New Input Total Price is 100 and Old Input Total Price is 200
     Then before update : Credit = 1000 + 200 = 1200
     And after update : Credit = 1200 + (100 - 200) = 1100
