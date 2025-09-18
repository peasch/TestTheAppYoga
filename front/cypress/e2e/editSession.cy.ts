describe('Login spec', () => {

  beforeEach(function () {
    cy.fixture('teachers.json').as('teachersData');
    cy.fixture('sessions.json').as('sessionsData');
  });



  it('Login successfull in order to edit session', function() {
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

    cy.intercept('GET', '/api/teacher', {
      body: this.teachersData
    });

    cy.intercept('GET', '/api/session/1', {
      body:  {
        "id": 1,
        "name": "Yoga Matinal",
        "date": "2024-12-05T08:30:00Z",
        "teacher_id": 1,
        "description": "Session de yoga matinale avec l'instructrice Anne.",
        "createdAt": "2024-12-01T10:00:00Z",
        "updatedAt": "2024-12-03T12:00:00Z"
      },
    });
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session');

    cy.intercept( 'GET', '/api/session',{
      body:this.sessionsData
    });
    cy.intercept('PUT', '/api/session/1', {
      status: 200,
    });
    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.url().should('include', '/sessions');

    cy.get('button span').contains("Edit").click()

    cy.url().should('include', '/sessions/update/1')

    cy.get('textarea[formControlName=description]').clear();
    cy.get('textarea[formControlName=description]')
      .type("Session avec Anne Pamanké, car Larry Golade est absent, pour faire du yoga Dougou, avec JP");

    cy.get('button[type=submit]').click();

    cy.url().should('include', '/sessions');
  })

  it('Login successfull in order to see session details and Delete button', function() {
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

    cy.intercept('GET', '/api/teacher', {
      body: this.teachersData
    });

    cy.intercept( 'GET', '/api/session',{
      body:this.sessionsData
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        id: 1,
        name: "Yoga Matinal",
        date: "2024-12-05T08:30:00Z",
        teacher_id: 1,
        users:[],
        description: "Session de yoga matinale avec l'instructrice Anne.",
        createdAt: "2024-12-01T10:00:00Z",
        updatedAt: "2024-12-03T12:00:00Z"
      }
    ).as('session')


    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.url().should('include', '/sessions');

    cy.get('button span').contains("Detail").should('be.visible');
    cy.get('button span').contains("Detail").click()
    cy.wait(500)
    cy.url().should('include', '/sessions/detail/1')
    cy.wait(500)
    cy.get('button span').contains("Delete").should('be.visible');

  })

  it('Login successfull no admin Session details and No Delete button', function() {
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

    cy.intercept('GET', '/api/teacher', {
      body: this.teachersData
    });

    cy.intercept( 'GET', '/api/session',{
      body:this.sessionsData
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        id: 1,
        name: "Yoga Matinal",
        date: "2024-12-05T08:30:00Z",
        teacher_id: 1,
        users:[],
        description: "Session de yoga matinale avec l'instructrice Anne.",
        createdAt: "2024-12-01T10:00:00Z",
        updatedAt: "2024-12-03T12:00:00Z"
      }
    ).as('session')


    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.url().should('include', '/sessions');

    cy.get('button span').contains("Detail").should('be.visible');
    cy.get('button span').contains("Detail").click()

    cy.url().should('include', '/sessions/detail/1')

    cy.get('button span').contains("Delete").should('not.exist');
    cy.get('button span').contains("Participate").should('be.visible');
  })
});
