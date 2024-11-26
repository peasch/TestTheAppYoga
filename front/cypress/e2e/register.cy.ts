describe('register spec',() =>{


  it('register successful',() =>{
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {
      body: {

        email: 'email',
        firstName: 'firstName',
        lastName: 'lastName',
        password:'password'
      },
    })

    cy.get('input[formControlName=firstName]').type("boby")
    cy.get('input[formControlName=lastName]').type("Lastbob")
    cy.get('input[formControlName=email]').type("boby@boby.fr")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/login')

  })

})
