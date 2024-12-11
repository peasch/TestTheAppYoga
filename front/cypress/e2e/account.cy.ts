describe('Login to account spec', () => {

  beforeEach(function () {
    cy.fixture('teachers.json').as('teachersData');
    cy.fixture('users.json').as('usersData');
  });

  it('Login successfull admin', function() {

    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })
    cy.intercept("GET", "/api/user/1",  {
     body:{
       id: 1,
       lastName: "PAMANKé",
       firstName: "Anne",
       password:'password',
       email:"anne.pamanké@studio.com",
       createdAt:  "2023-01-15T10:30:00Z",
       updatedAt: "2023-11-25T14:45:00Z"
     }
    }).as('user');

    cy.intercept( 'GET', '/api/session',{
      body:this.sessionsData
    });

    cy.get('input[formControlName=email]').type("anne.pamanké@studio.com")
    cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`)

    cy.url().should('include', '/sessions')

    cy.contains('Account').click();

  });
});
