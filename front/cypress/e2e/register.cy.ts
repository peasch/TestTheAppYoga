describe('register spec',() =>{


  it('register successful',() =>{
    const newUser = {
      id: 3, // Assurez-vous que l'ID est unique
      firstName: "boby",
      lastName: "Lastbob",
      email: "boby@boby.fr",
      password:"test!1234",
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    };
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {
      body: {
        email: newUser.email,
        firstName: newUser.firstName,
        lastName: newUser.lastName,
        password: newUser.password,
      },
    });

    cy.get('input[formControlName=firstName]').type(newUser.firstName)
    cy.get('input[formControlName=lastName]').type(newUser.lastName)
    cy.get('input[formControlName=email]').type(newUser.email)
    cy.get('input[formControlName=password]').type(`${newUser.password}{enter}{enter}`)

    cy.url().should('include', '/login')


  })

})
