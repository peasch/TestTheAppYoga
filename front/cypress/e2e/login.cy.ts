describe('Login spec', () => {

  beforeEach(function () {
    cy.fixture('teachers.json').as('teachersData');
    cy.fixture('sessions.json').as('sessionsData');
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

    cy.intercept( 'GET', '/api/session',{
      body:this.sessionsData
    });

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })

  it('Login successfull not admin', function() {

    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false
      },
    })

    cy.intercept( 'GET', '/api/session',{
      body:this.sessionsData
    });

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
    cy.get('button span').contains("Edit").should('not.exist');
    cy.get('span[class=link]').contains("Logout").click()

    cy.url().should('eq', 'http://localhost:4200/')
  })


  it('Login error with bad email', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401
    })

    cy.get('input[formControlName=email]').type("yogo@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!"}`)
    cy.get('button[type=submit]').click()
    cy.wait(500)
    cy.get('p').contains('An error occurred')
  })
});


